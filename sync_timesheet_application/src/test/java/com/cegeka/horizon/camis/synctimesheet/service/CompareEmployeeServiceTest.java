package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder;
import com.cegeka.horizon.camis.timesheet.testbuilder.TestLocalDates;
import com.cegeka.horizon.camis.timesheet.testbuilder.TestWorkOrders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.cegeka.horizon.camis.timesheet.testbuilder.EmployeeTestBuilder.anEmployee;
import static com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestLocalDates.WEEK_0_MONDAY;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TestWorkOrders.WORK_ORDER_1;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;
import static org.assertj.core.api.Assertions.assertThat;

class CompareEmployeeServiceTest {

    @Test
    public void givenExactTimesheetLine_whenCompare_NoAction(){
        Employee inputEmployee = anEmployee()
                .withTimeSheet(aWeeklyTimesheet()
                        .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY)))
                ).build();

        Employee camisEmployee = anEmployee()
                .withTimeSheet(aWeeklyTimesheet()
                        .withLine(aTimesheetLine().withWorkOrder(WORK_ORDER_1).withLoggedHours(aLoggedHours(6, WEEK_0_MONDAY)))
                ).build();

        assertThat(new CompareEmployeeService().compare(inputEmployee, camisEmployee)).isEmpty();
    }

    public void givenIncorrectYetDeleteableTimesheetLine_whenCompare_DeleteCommand(){

    }

    public void givenIncompleteTimesheetLine_whenCompare_AddCommand(){

    }

    public void givenUnexistingTimesheetLine_whenCompare_AddCommand(){

    }

}