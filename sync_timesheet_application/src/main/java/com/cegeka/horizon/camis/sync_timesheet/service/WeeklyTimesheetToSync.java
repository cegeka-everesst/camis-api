package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;

public record WeeklyTimesheetToSync(Employee employee, WeeklyTimesheet timesheet) {

}
