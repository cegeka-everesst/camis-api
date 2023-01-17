package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.testbuilder.*;
import org.junit.jupiter.api.Test;

import static com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestLocalDates.*;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestWorkOrders.WORK_ORDER_1;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    @Test
    public void givenWeeklyTimesheets_whenSameWeek_thenMerge(){
        WeeklyTimesheet weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_1_TUESDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_1_WEDNESDAY))
                        )
                .build();

        WeeklyTimesheet sameWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_1_THURSDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_1_FRIDAY))
                        )
                .build();

        Employee employee = new Employee(new ResourceId("I122321"), "Ward");
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(sameWeek);

        assertThat(employee.weeklyTimesheets()).hasSize(1);
    }

    @Test
    public void givenWeeklyTimesheets_whenDifferenWeek_thenMerge(){
        WeeklyTimesheet weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_1_TUESDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_1_WEDNESDAY))
                        )
                .build();

        WeeklyTimesheet differentWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_2_MONDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_2_TUESDAY))
                        )
                .build();

        Employee employee = new Employee(new ResourceId("I122312"), "Ward");
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(differentWeek);

        assertThat(employee.weeklyTimesheets()).hasSize(2);
    }

    @Test
    public void givenTimesheets_whenGetTimesheetPeriod_thenReturnTotalDuration(){
        WeeklyTimesheet weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_0_MONDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_0_WEDNESDAY))
                        )
                .build();

        WeeklyTimesheet differentWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_2_MONDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_2_TUESDAY))
                        )
                .build();

        Employee employee = new Employee(new ResourceId("I122321"), "Ward");
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(differentWeek);

        assertThat(employee.getTimesheetDurations().getStart()).isEqualTo(WEEK_0_MONDAY);
        assertThat(employee.getTimesheetDurations().getEnd()).isEqualTo(WEEK_2_SUNDAY);
    }

    @Test
    public void givenExternalAndTimeCodeNoAssignment_whenIsTooSync_thenReturnTrue() {
        Employee externalEmployee = EmployeeTestBuilder.anEmployee().withIdentifier("I123123").build();
        assertThat(externalEmployee.isToSync(TimeCode.NO_ASSIGNMENT)).isTrue();
        assertThat(externalEmployee.isToSync(TimeCode.WORK_DAY)).isTrue();
    }

    @Test
    public void givenExternalAndTimeCodeNoAssignment_whenIsTooSync_thenReturnFalse() {
        Employee internalEmployee = EmployeeTestBuilder.anEmployee().withIdentifier("9090290").build();
        assertThat(internalEmployee.isToSync(TimeCode.NO_ASSIGNMENT)).isFalse();
        assertThat(internalEmployee.isToSync(TimeCode.WORK_DAY)).isTrue();
    }


}
