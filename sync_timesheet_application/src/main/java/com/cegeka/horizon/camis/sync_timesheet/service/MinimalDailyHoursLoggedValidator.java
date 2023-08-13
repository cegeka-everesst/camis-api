package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.sync_logger.model.SyncResult;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.Employee;
import org.threeten.extra.LocalDateRange;

import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE;

public class MinimalDailyHoursLoggedValidator {

    private final double minimumHoursLogged;
    private final SyncLoggerService loggerService;

    public MinimalDailyHoursLoggedValidator(double minimumHoursLogged, SyncLoggerService syncLoggerService) {
        this.minimumHoursLogged = minimumHoursLogged;
        this.loggerService = syncLoggerService;
    }

    void validate(List<Employee> inputEmployees, SyncResult syncResult) {
        LocalDateRange dateRange = determineMaximumPeriod(inputEmployees);

        inputEmployees
            .forEach(inputEmployee -> dateRange.stream()
                    .forEach(date -> {
                        if(! inputEmployee.hasMinimumDailyHoursLogged(date, minimumHoursLogged)){
                            syncResult.addSyncRecord(
                                    loggerService.logAndAddSyncRecordWithHoursMinimum(inputEmployee.id(), new RecordData(date, String.format("Less than %1$.1f logged on %2", minimumHoursLogged, date.format(ISO_DATE))), minimumHoursLogged));
                        }
                    }));
    }

    private LocalDateRange determineMaximumPeriod(List<Employee> inputEmployees) {
        return inputEmployees.stream()
                .map(Employee::loggedHoursRange)
                .reduce(new LocalDateRangeOperator()).get();
    }

    public static class LocalDateRangeOperator implements java.util.function.BinaryOperator<LocalDateRange> {
        @Override
        public LocalDateRange apply(LocalDateRange range1, LocalDateRange range2) {
            return range1.union(range2);
        }
    }

}
