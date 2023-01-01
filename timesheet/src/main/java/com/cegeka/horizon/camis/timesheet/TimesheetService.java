package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public interface TimesheetService {
    Employee getTimesheetEntries(ResourceId resourceId, String employeeName, LocalDate dateFrom, LocalDate dateTo);

    TimesheetLineIdentifier createTimesheetEntry(ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay);

    TimesheetLineIdentifier updateTimesheetEntry(TimesheetLineIdentifier timesheetLineIdentifier, ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHours);

}
