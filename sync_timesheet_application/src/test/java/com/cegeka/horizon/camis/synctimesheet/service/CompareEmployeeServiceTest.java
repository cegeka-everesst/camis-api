package com.cegeka.horizon.camis.synctimesheet.service;

import static com.cegeka.horizon.camis.timesheet.testbuilder.EmployeeTestBuilder.anEmployee;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static com.cegeka.horizon.camis.timesheet.testbuilder.WeeklyTimesheetTestBuilder.aWeeklyTimesheet;

class CompareEmployeeServiceTest {

    public void givenExactTimesheetLine_whenCompare_NoAction(){
        anEmployee()
                .withTimeSheet(aWeeklyTimesheet()
                                        .withLine(aTimesheetLine())
                                        );
    }

    public void givenIncorrectYetDeleteableTimesheetLine_whenCompare_DeleteCommand(){

    }

    public void givenIncompleteTimesheetLine_whenCompare_AddCommand(){

    }

    public void givenUnexistingTimesheetLine_whenCompare_AddCommand(){

    }

}