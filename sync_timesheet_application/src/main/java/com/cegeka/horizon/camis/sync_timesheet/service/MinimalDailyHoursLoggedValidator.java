package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.sync_logger.model.SyncResult;
import com.cegeka.horizon.camis.timesheet.Employee;
import org.threeten.extra.LocalDateRange;

import java.util.List;

public class MinimalDailyHoursLoggedValidator {

    private final double minimumHoursLogged;

    public MinimalDailyHoursLoggedValidator(double minimumHoursLogged) {
        this.minimumHoursLogged = minimumHoursLogged;
    }

    void validate(List<Employee> inputEmployees, SyncResult syncResult) {
        LocalDateRange dateRange = determineMaximumPeriod(inputEmployees);

        inputEmployees
            .stream()
            .forEach(inputEmployee -> dateRange.stream()
                    .forEach(date -> inputEmployee.hasMinimumDailyHoursLogged(date, minimumHoursLogged)));
    }

    private LocalDateRange determineMaximumPeriod(List<Employee> inputEmployees) {
        return inputEmployees.stream()
                .map(inputEmployee -> inputEmployee.loggedHoursRange())
                .reduce(new LocalDateRangeOperator()).get();
    }

    public static class LocalDateRangeOperator implements java.util.function.BinaryOperator<LocalDateRange> {
        @Override
        public LocalDateRange apply(LocalDateRange range1, LocalDateRange range2) {
            return range1.union(range2);
        }
    }

}
