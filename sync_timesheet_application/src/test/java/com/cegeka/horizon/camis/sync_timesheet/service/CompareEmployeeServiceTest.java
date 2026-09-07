package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.sync_timesheet.service.command.CreateTimesheetEntryCommand;
import com.cegeka.horizon.camis.sync_timesheet.service.command.ErrorCommand;
import com.cegeka.horizon.camis.sync_timesheet.service.command.NothingToSyncCommand;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.cegeka.horizon.camis.timesheet.builder.EmployeeTestBuilder.anEmployee;
import static com.cegeka.horizon.camis.timesheet.builder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.builder.TestLocalDates.WEEK_0_MONDAY;
import static com.cegeka.horizon.camis.timesheet.builder.TestLocalDates.WEEK_0_TUESDAY;
import static com.cegeka.horizon.camis.timesheet.builder.TestLocalDates.WEEK_0_WEDNESDAY;
import static com.cegeka.horizon.camis.timesheet.builder.TestWorkOrders.WORK_ORDER_1;
import static com.cegeka.horizon.camis.timesheet.builder.TestWorkOrders.WORK_ORDER_2;
import static com.cegeka.horizon.camis.timesheet.builder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.builder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;

class CompareEmployeeServiceTest {

    @Test
    void givenExactTimesheetLine_whenCompare_NoAction() {
        WeeklyTimesheet inputTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY))).build();

        WeeklyTimesheet camisTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY))).build();

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet))).hasSize(1);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0)).isInstanceOf(NothingToSyncCommand.class);
    }

    @Test
    void givenMoreHoursTimesheetLine_whenCompare_ErrorCommand() {
        WeeklyTimesheet inputTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(
                        aLoggedHours(6, WEEK_0_MONDAY))).build();

        Employee inputEmployee = anEmployee().build();

        WeeklyTimesheet camisTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(
                        aLoggedHours(8, WEEK_0_MONDAY))).build();

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet))).hasSize(1);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0)).isInstanceOf(ErrorCommand.class);
    }

    @Test
    void giveIncompleteTimesheetLineWithMissingDay_whenCompare_AddCommand() {
        WeeklyTimesheet inputTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(
                        aLoggedHours(6, WEEK_0_MONDAY),
                        aLoggedHours(8, WEEK_0_TUESDAY))).build();

        WeeklyTimesheet camisTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(
                        aLoggedHours(6, WEEK_0_MONDAY))).build();

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet))).hasSize(2);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0)).isInstanceOf(NothingToSyncCommand.class);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(1)).isInstanceOf(CreateTimesheetEntryCommand.class);

        CreateTimesheetEntryCommand createTimesheetEntryCommand = (CreateTimesheetEntryCommand) new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(1);
        assertThat(createTimesheetEntryCommand.loggedHours()).isEqualTo(aLoggedHours(8, WEEK_0_TUESDAY).build());
    }

    @Test
    void givenIncompleteTimesheetLineWithMissingDay_whenCompare_AddCommand() {
        WeeklyTimesheet inputTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(
                        aLoggedHours(6, WEEK_0_MONDAY),
                        aLoggedHours(8, WEEK_0_TUESDAY))).build();

        WeeklyTimesheet camisTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(
                        aLoggedHours(6, WEEK_0_MONDAY))).build();

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet))).hasSize(2);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0)).isInstanceOf(NothingToSyncCommand.class);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(1)).isInstanceOf(CreateTimesheetEntryCommand.class);

        CreateTimesheetEntryCommand createTimesheetEntryCommand = (CreateTimesheetEntryCommand) new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(1);
        assertThat(createTimesheetEntryCommand.loggedHours()).isEqualTo(aLoggedHours(8, WEEK_0_TUESDAY).build());
    }

    @Test
    void givenIncompleteTimesheetLineWithMissingHoursOnDay_whenCompare_AddCommand() {
        WeeklyTimesheet inputTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(8, WEEK_0_MONDAY))).build();

        WeeklyTimesheet camisTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY))).build();

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet))).hasSize(1);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0)).isInstanceOf(CreateTimesheetEntryCommand.class);

        CreateTimesheetEntryCommand createTimesheetEntryCommand = (CreateTimesheetEntryCommand) new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0);
        assertThat(createTimesheetEntryCommand.loggedHours()).isEqualTo(aLoggedHours(2, WEEK_0_MONDAY).build());
    }

    @Test
    void givenIncompleteTimesheetLineWithMissingHoursOnDayDifferentWorkOrder_whenCompare_AddCommand() {
        WeeklyTimesheet inputTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(7, WEEK_0_MONDAY)),
                        aTimesheetLine().withWorkOrder(WORK_ORDER_2).withLoggedHours(aLoggedHours(3, WEEK_0_MONDAY))).build();

        WeeklyTimesheet camisTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY))).build();

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet))).hasSize(2);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0)).isInstanceOf(CreateTimesheetEntryCommand.class);

        CreateTimesheetEntryCommand createTimesheetEntryCommand = (CreateTimesheetEntryCommand) new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(0);
        assertThat(createTimesheetEntryCommand.loggedHours()).isEqualTo(aLoggedHours(1, WEEK_0_MONDAY).build());
        assertThat(createTimesheetEntryCommand.workOrder()).isEqualTo(WORK_ORDER_1);

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(1)).isInstanceOf(CreateTimesheetEntryCommand.class);
        createTimesheetEntryCommand = (CreateTimesheetEntryCommand) new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, of(camisTimesheet)).get(1);
        assertThat(createTimesheetEntryCommand.loggedHours()).isEqualTo(aLoggedHours(3, WEEK_0_MONDAY).build());
        assertThat(createTimesheetEntryCommand.workOrder()).isEqualTo(WORK_ORDER_2);
    }

    @Test
    void givenUnexistingTimesheetLine_whenCompare_AddCommand() {

        WeeklyTimesheet inputTimesheet = aWeeklyTimesheet()
                .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1)
                        .withLoggedHours(
                                aLoggedHours(6, WEEK_0_MONDAY),
                                aLoggedHours(8, WEEK_0_WEDNESDAY))).build();

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, Optional.empty())).hasSize(2);
        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, Optional.empty()).get(0)).isInstanceOf(CreateTimesheetEntryCommand.class);
        CreateTimesheetEntryCommand createTimesheetEntryCommand = (CreateTimesheetEntryCommand) new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, Optional.empty()).get(0);
        assertThat(createTimesheetEntryCommand.loggedHours()).isEqualTo(aLoggedHours(6, WEEK_0_MONDAY).build());

        assertThat(new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, Optional.empty()).get(1)).isInstanceOf(CreateTimesheetEntryCommand.class);
        createTimesheetEntryCommand = (CreateTimesheetEntryCommand) new CompareEmployeeService().compare(anEmployee().build(), inputTimesheet, Optional.empty()).get(1);
        assertThat(createTimesheetEntryCommand.loggedHours()).isEqualTo(aLoggedHours(8, WEEK_0_WEDNESDAY).build());
    }
}