package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;

public record WeeklyTimesheetToSync(Employee employee, WeeklyTimesheet timesheet) {

}
