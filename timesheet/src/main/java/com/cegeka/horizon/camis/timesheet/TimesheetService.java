package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.api.post.CreateTimesheetEntryResult;

import java.time.LocalDate;

public interface TimesheetService {
    Employee getTimesheetEntries(ResourceId resourceId, String employeeName, LocalDate dateFrom, LocalDate dateTo);

    boolean createTimesheetEntry(ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay);
}
