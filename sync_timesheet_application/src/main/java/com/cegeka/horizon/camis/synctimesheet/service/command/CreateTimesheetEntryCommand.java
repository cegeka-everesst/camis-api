package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;
import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetService;

public class CreateTimesheetEntryCommand implements SyncCommand {
    private final String name;
    private final ResourceId resourceId;
    private final WorkOrder workOrder;
    private final LoggedHoursByDay loggedHoursByDay;
    private final TimeCode timeCode;

    public CreateTimesheetEntryCommand(String name, ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay, TimeCode timeCode) {
        this.name = name;
        this.resourceId = resourceId;
        this.workOrder = workOrder;
        this.loggedHoursByDay = loggedHoursByDay;
        this.timeCode = timeCode;
    }

    @Override
    public void execute(TimesheetService timesheetService, SyncLoggerService syncLoggerService) {
        try {
            timesheetService.createTimesheetEntry(resourceId, timeCode, workOrder, loggedHoursByDay);
            syncLoggerService.logAndAddSyncRecord("Updated timesheetLine of employee " + name + " on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(), name, loggedHoursByDay.date(), loggedHoursByDay.hours(), workOrder);
        } catch (Exception e) {
            if (resourceId.isExternal() && timeCode.equals(TimeCode.NO_ASSIGNMENT)) {
                syncLoggerService.logAndAddSyncRecord("Could not update timesheetLine of external employee " + name + " BUT OKAY on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(), name, loggedHoursByDay.date(), loggedHoursByDay.hours(), workOrder);
            } else {
                syncLoggerService.logAndAddSyncRecord("Error occurred when trying to update timesheetLine of employee " + name + " on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(), name, loggedHoursByDay.date(), loggedHoursByDay.hours(), workOrder);
            }
        }
    }

    public LoggedHoursByDay loggedHours() {
        return loggedHoursByDay;
    }

    public WorkOrder workOrder() {
        return workOrder;
    }

}
