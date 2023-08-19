package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;
import com.cegeka.horizon.camis.timesheet.testbuilder.EmployeeTestBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.threeten.extra.LocalDateRange;

import java.util.List;

import static com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestLocalDates.*;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestWorkOrders.WORK_ORDER_1;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;
import static org.assertj.core.api.Assertions.assertThat;

class MinimalDailyHoursLoggedValidatorTest {
    @Mock private SyncLoggerService syncLoggerService;

    @Test
    public void givenEmployeesWithConnectingPeriods_thenUnion(){

        Employee employee1 = EmployeeTestBuilder.anEmployee().withTimeSheet(aWeeklyTimesheet()
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY)))
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_WEDNESDAY)))).build();

        Employee employee2 = EmployeeTestBuilder.anEmployee().withTimeSheet(aWeeklyTimesheet()
                        .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_WEDNESDAY)))
                        .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_1_FRIDAY))))
                .build();

        List<Employee> employees = List.of(employee1, employee2);

        LocalDateRange dateRange =
                new MinimalDailyHoursLoggedValidator(8, syncLoggerService).determineMaximumPeriod(employees);
        assertThat(dateRange.getStart()).isEqualTo(WEEK_0_MONDAY);
        assertThat(dateRange.getEndInclusive()).isEqualTo(WEEK_1_FRIDAY);
    }

    @Test
    public void givenEmployeesWithNonConnectingPeriods_thenUnionOfAbsoluteStartAndStop(){

        Employee employee1 = EmployeeTestBuilder.anEmployee().withTimeSheet(aWeeklyTimesheet()
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_TUESDAY)))
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_WEDNESDAY)))).build();

        Employee employee2 = EmployeeTestBuilder.anEmployee().withTimeSheet(aWeeklyTimesheet()
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_1_THURSDAY)))
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_1_FRIDAY))))
                .build();

        List<Employee> employees = List.of(employee1, employee2);

        LocalDateRange dateRange =
                new MinimalDailyHoursLoggedValidator(8, syncLoggerService).determineMaximumPeriod(employees);
        assertThat(dateRange.getStart()).isEqualTo(WEEK_0_TUESDAY);
        assertThat(dateRange.getEndInclusive()).isEqualTo(WEEK_1_FRIDAY);
    }

    @Test
    public void givenEmployeesWithInnerPeriods_thenUnionOfOuter(){

        Employee employee1 = EmployeeTestBuilder.anEmployee().withTimeSheet(aWeeklyTimesheet()
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_TUESDAY)))
                .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_WEDNESDAY)))).build();

        Employee employee2 = EmployeeTestBuilder.anEmployee().withTimeSheet(aWeeklyTimesheet()
                        .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY)))
                        .withLine(aTimesheetLine().withLoggedHours(aLoggedHours(6, WEEK_1_FRIDAY))))
                .build();

        List<Employee> employees = List.of(employee1, employee2);

        LocalDateRange dateRange =
                new MinimalDailyHoursLoggedValidator(8, syncLoggerService).determineMaximumPeriod(employees);
        assertThat(dateRange.getStart()).isEqualTo(WEEK_0_MONDAY);
        assertThat(dateRange.getEndInclusive()).isEqualTo(WEEK_1_FRIDAY);
    }
}