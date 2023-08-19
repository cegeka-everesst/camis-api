package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync.logger.model.result.CamisWorkorderInfo;
import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
import com.cegeka.horizon.camis.sync.logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;
import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

public class CreateTimesheetEntryCommand implements SyncCommand {
    private final EmployeeIdentification employeeId;
    private final WorkOrder workOrder;
    private final LoggedHoursByDay loggedHoursByDay;
    private final TimeCode timeCode;

    public CreateTimesheetEntryCommand(EmployeeIdentification employeeId, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay, TimeCode timeCode) {
        this.employeeId = employeeId;
        this.workOrder = workOrder;
        this.loggedHoursByDay = loggedHoursByDay;
        this.timeCode = timeCode;
    }

    @Override
    public SyncResult execute(WebClient webClient, TimesheetService timesheetService, SyncLoggerService syncLoggerService) {
        try {
            timesheetService.createTimesheetEntry(webClient, employeeId.resourceId(), timeCode, workOrder, loggedHoursByDay);
            return syncLoggerService.logAndAddSyncRecordWithUpdate(employeeId, new CamisWorkorderInfo(loggedHoursByDay.date(),"Updated timesheetLine of employee " + employeeId.name() + " on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(),  workOrder), loggedHoursByDay.hours());
        } catch (Exception e) {
            if (employeeId.resourceId().isExternal() && timeCode.equals(TimeCode.NO_ASSIGNMENT)) {
                return syncLoggerService.logAndAddSyncRecordWithUpdate(employeeId, new CamisWorkorderInfo(loggedHoursByDay.date(),"Could not update timesheetLine of external employee " + employeeId.name() + " BUT OKAY on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(), workOrder), loggedHoursByDay.hours());
            } else {
                return syncLoggerService.logAndAddSyncRecordWithUpdate(employeeId, new CamisWorkorderInfo(loggedHoursByDay.date(), "Error occurred when trying to update timesheetLine of employee " + employeeId.name() + " on date " + loggedHoursByDay.date() + " with workOrder " + workOrder.value() + " and hours " + loggedHoursByDay.hours(), workOrder), loggedHoursByDay.hours());
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
