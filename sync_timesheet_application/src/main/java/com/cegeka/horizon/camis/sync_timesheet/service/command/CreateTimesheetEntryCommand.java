package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.model.syncrecord.SyncRecord;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;
import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

public class CreateTimesheetEntryCommand implements SyncCommand {
    private final EmployeeData employerData;
    private final WorkOrder workOrder;
    private final LoggedHoursByDay loggedHoursByDay;
    private final TimeCode timeCode;

    public CreateTimesheetEntryCommand(EmployeeData employerData, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay, TimeCode timeCode) {
        this.employerData = employerData;
        this.workOrder = workOrder;
        this.loggedHoursByDay = loggedHoursByDay;
        this.timeCode = timeCode;
    }

    @Override
    public SyncRecord execute(WebClient webClient, TimesheetService timesheetService, SyncLoggerService syncLoggerService) {
        try {
            timesheetService.createTimesheetEntry(webClient, employerData.getResourceId(), timeCode, workOrder, loggedHoursByDay);
            return syncLoggerService.logAndAddSyncRecordWithUpdate(employerData, new RecordData(loggedHoursByDay.date(),"Updated timesheetLine of employee " + employerData.getEmployeeName() + " on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(),  workOrder), loggedHoursByDay.hours());
        } catch (Exception e) {
            if (employerData.getResourceId().isExternal() && timeCode.equals(TimeCode.NO_ASSIGNMENT)) {
                return syncLoggerService.logAndAddSyncRecordWithUpdate(employerData, new RecordData(loggedHoursByDay.date(),"Could not update timesheetLine of external employee " + employerData.getEmployeeName() + " BUT OKAY on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(), workOrder), loggedHoursByDay.hours());
            } else {
                return syncLoggerService.logAndAddSyncRecordWithUpdate(employerData, new RecordData(loggedHoursByDay.date(), "Error occurred when trying to update timesheetLine of employee " + employerData.getEmployeeName() + " on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(), workOrder), loggedHoursByDay.hours());
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
