package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.timesheet.testbuilder.EmployeeTestBuilder;

class CompareEmployeeServiceTest {

    public void givenExactTimesheetLine_whenCompare_NoAction(){
        EmployeeTestBuilder.anEmployee();
    }

    public void givenIncorrectYetDeleteableTimesheetLine_whenCompare_DeleteCommand(){

    }

    public void givenIncompleteTimesheetLine_whenCompare_AddCommand(){

    }

    public void givenUnexistingTimesheetLine_whenCompare_AddCommand(){

    }

}