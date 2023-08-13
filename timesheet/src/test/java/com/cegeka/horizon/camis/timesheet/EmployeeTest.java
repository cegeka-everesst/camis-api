package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.testbuilder.EmployeeTestBuilder;
import com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder;
import org.junit.jupiter.api.Test;

import static com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestLocalDates.*;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestWorkOrders.WORK_ORDER_1;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestWorkOrders.WORK_ORDER_2;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;
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


        Employee employee = new Employee(new EmployeeIdentification(new ResourceId("I122312"), "Ward"));
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(sameWeek);

        assertThat(employee.weeklyTimesheets()).hasSize(1);
    }

    @Test
    public void givenWeeklyTimesheets_whenDifferentWeek_thenMerge(){
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

        Employee employee = new Employee(new EmployeeIdentification(new ResourceId("I122312"), "Ward"));
        employee.addWeeklyTimesheet(weeklyTimesheet);
        employee.addWeeklyTimesheet(differentWeek);

        assertThat(employee.weeklyTimesheets()).hasSize(2);
    }

    @Test
    public void givenTimesheets_whenGetTimesheetPeriod_thenReturnTotalDuration(){
        WeeklyTimesheetTestBuilder weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_0_MONDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_0_WEDNESDAY))
                        );

        WeeklyTimesheetTestBuilder differentWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_2_MONDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_2_TUESDAY))
                        );

        Employee employee = EmployeeTestBuilder.anEmployee()
                .withTimeSheet(weeklyTimesheet, differentWeek).build();

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

    @Test
    public void givenEmployee_whenLoggedHoursRange_thenRange(){
        WeeklyTimesheetTestBuilder weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_1_TUESDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_1_WEDNESDAY))
                );

        WeeklyTimesheetTestBuilder differentWeek = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_2_MONDAY))
                        .withLoggedHours(aLoggedHours(5, WEEK_2_TUESDAY))
                );

        Employee employee = EmployeeTestBuilder.anEmployee()
                .withTimeSheet(weeklyTimesheet, differentWeek).build();

        assertThat(employee.loggedHoursRange().getStart()).isEqualTo(WEEK_1_TUESDAY);
        assertThat(employee.loggedHoursRange().getEndInclusive()).isEqualTo(WEEK_2_TUESDAY);
    }

    @Test
    public void givenEmployee_hasMinimumDailyHoursLogged_thenCheckSum(){
        WeeklyTimesheetTestBuilder weeklyTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(aLoggedHours(5, WEEK_1_TUESDAY))
                        .withLoggedHours(aLoggedHours(9, WEEK_1_WEDNESDAY))
                        .withLoggedHours(aLoggedHours(8, WEEK_1_FRIDAY))
                )
                .withLine(aTimesheetLine()
                        .withWorkOrder(WORK_ORDER_2)
                        .withLoggedHours(aLoggedHours(4, WEEK_1_MONDAY))
                        .withLoggedHours(aLoggedHours(3, WEEK_1_TUESDAY))
                );

        Employee employee = EmployeeTestBuilder.anEmployee()
                .withTimeSheet(weeklyTimesheet).build();

        assertThat(employee.hasMinimumDailyHoursLogged(WEEK_1_MONDAY, 8)).isEqualTo(true);
        assertThat(employee.hasMinimumDailyHoursLogged(WEEK_1_TUESDAY, 8)).isEqualTo(true);
        assertThat(employee.hasMinimumDailyHoursLogged(WEEK_1_WEDNESDAY, 8)).isEqualTo(true);
        assertThat(employee.hasMinimumDailyHoursLogged(WEEK_1_FRIDAY, 8)).isEqualTo(true);
    }

}
