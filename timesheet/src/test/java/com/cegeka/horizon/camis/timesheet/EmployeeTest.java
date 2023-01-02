package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.testbuilder.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    @Test
    public void givenWeeklyTimesheets_whenSameWeek_thenMerge(){
        WeeklyTimesheet weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(TestWorkOrders.WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 6)))
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 7)))
                        .build())
                .build();

        WeeklyTimesheet sameWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(TestWorkOrders.WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 8)))
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 9)))
                        .build())
                .build();

        Employee employee = new Employee(new ResourceId("I12231"), "Ward");
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(sameWeek);

        assertThat(employee.weeklyTimesheets()).hasSize(1);
    }

    @Test
    public void givenWeeklyTimesheets_whenDifferenWeek_thenMerge(){
        WeeklyTimesheet weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(TestWorkOrders.WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 6)))
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 7)))
                        .build())
                .build();

        WeeklyTimesheet differentWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(TestWorkOrders.WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 12)))
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 13)))
                        .build())
                .build();

        Employee employee = new Employee(new ResourceId("I12231"), "Ward");
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(differentWeek);

        assertThat(employee.weeklyTimesheets()).hasSize(2);
    }

    @Test
    public void givenTimesheets_whenGetTimesheetPeriod_thenReturnTotalDuration(){
        WeeklyTimesheet weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(TestWorkOrders.WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, of(2022, 11, 29)))
                        .withLoggedHours(aLoggedHours(5, of(2022, 11, 30)))
                        .build())
                .build();

        WeeklyTimesheet differentWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(TestWorkOrders.WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 12)))
                        .withLoggedHours(aLoggedHours(5, of(2022, 12, 13)))
                        .build())
                .build();

        Employee employee = new Employee(new ResourceId("I12231"), "Ward");
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(differentWeek);

        assertThat(employee.getTimesheetDurations().getStart()).isEqualTo(of(2022,11,28));
        assertThat(employee.getTimesheetDurations().getEnd()).isEqualTo(of(2022,12,18));
    }

    @Test
    public void givenExternalAndTimeCodeNoAssignment_whenIsTooSync_thenReturnTrue() {
        Employee externalEmployee = EmployeeTestBuilder.anEmployee().withIdentifier("I123123").build();
        assertThat(externalEmployee.isToSync(TimeCode.NO_ASSIGNMENT)).isTrue();
        assertThat(externalEmployee.isToSync(TimeCode.WORK_DAY)).isTrue();
    }

    @Test
    public void givenExternalAndTimeCodeNoAssignment_whenIsTooSync_thenReturnFalse() {
        Employee internalEmployee = EmployeeTestBuilder.anEmployee().withIdentifier("909090").build();
        assertThat(internalEmployee.isToSync(TimeCode.NO_ASSIGNMENT)).isFalse();
        assertThat(internalEmployee.isToSync(TimeCode.WORK_DAY)).isTrue();
    }


}
