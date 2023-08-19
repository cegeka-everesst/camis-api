package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.sync_logger.model.SyncResult;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.Employee;
import org.threeten.extra.LocalDateRange;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

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
                    .filter(isWeekend().negate())
                    .forEach(date -> {
                        if(! inputEmployee.hasMinimumDailyHoursLogged(date, minimumHoursLogged)){
                            syncResult.addSyncRecord(
                                    loggerService.logAndAddSyncRecordWithHoursMinimum(inputEmployee.id(), new RecordData(date, String.format("Less than %.1f hours logged on %s by %s", minimumHoursLogged, date.format(ISO_DATE), inputEmployee.name())), minimumHoursLogged));
                        }
                    }));
    }

    private static Predicate<LocalDate> isWeekend() {
        return date -> date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    LocalDateRange determineMaximumPeriod(List<Employee> inputEmployees) {
        return inputEmployees.stream()
                .map(Employee::loggedHoursRange)
                .reduce(new JoiningLocalDateRangeOperator()).get();
    }

    public static class JoiningLocalDateRangeOperator implements java.util.function.BinaryOperator<LocalDateRange> {
        @Override
        public LocalDateRange apply(LocalDateRange range1, LocalDateRange range2) {
            if(range1.isConnected(range2)){
                return range1.union(range2);
            }else{
                if(range1.isBefore(range2)){
                    return LocalDateRange.of(range1.getStart(), range2.getEnd());
                }else{
                    return LocalDateRange.of(range2.getStart(), range1.getEnd());
                }
            }
        }
    }

}
