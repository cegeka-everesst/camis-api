package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync.logger.model.result.CamisWorkorderInfo;
import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
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
    public SyncResult execute(WebClient webClient, TimesheetService timesheetService) {
        try {
            timesheetService.createTimesheetEntry(webClient, employeeId.resourceId(), timeCode, workOrder, loggedHoursByDay);
            return SyncResult.success(employeeId, new CamisWorkorderInfo(loggedHoursByDay.date(),
                    String.format("Updated timesheetLine of employee %s on date %s for work order %s (%s hours)",
                            employeeId.resourceId(), loggedHoursByDay.date(), workOrder.value(), loggedHoursByDay.hours()),  workOrder)
                            , loggedHoursByDay.hours());
        } catch (Exception e) {
            if (employeeId.resourceId().isExternal() && timeCode.equals(TimeCode.NO_ASSIGNMENT)) {
                return SyncResult.warning(employeeId, new CamisWorkorderInfo(loggedHoursByDay.date(),
                        String.format("Could not update timesheetLine of external employee %s on date %s BUT for external employees it's okay" ,
                                employeeId.resourceId(), loggedHoursByDay.date()), workOrder)
                            , loggedHoursByDay.hours());
            } else {
                return SyncResult.updateTimesheetLineSyncError(employeeId, new CamisWorkorderInfo(loggedHoursByDay.date(),
                        String.format("Error occurred when trying to update timesheetLine of " +
                                        "employee %s on date %s for work order %s, " +
                                        "probably the timesheet is already submitted. Contact Cegeka accounting to open up your Camis week & fix manually.",
                                employeeId.resourceId(), loggedHoursByDay.date(), workOrder.value()), workOrder), loggedHoursByDay.hours());
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
