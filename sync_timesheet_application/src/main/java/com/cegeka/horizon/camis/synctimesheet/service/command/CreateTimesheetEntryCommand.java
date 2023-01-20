package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;
import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTimesheetEntryCommand implements SyncCommand {

    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

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
    public void execute(TimesheetService timesheetService) {
        try{
            timesheetService.createTimesheetEntry(resourceId, timeCode, workOrder, loggedHoursByDay);
            logger.info("Updated timesheetLine of employee {} on date {} with workOrder {} and hours {} ", name, loggedHoursByDay.date(), workOrder.value(), loggedHoursByDay.hours());
        }catch (Exception e){
            if(resourceId.isExternal() && timeCode.equals(TimeCode.NO_ASSIGNMENT)){
                logger.warn("Could not update timesheetLine of external employee {} BUT OKAY on date {} with workOrder {} and hours {} ", name, loggedHoursByDay.date(), workOrder.value(), loggedHoursByDay.hours());
            }else{
                logger.error("Error occurred when trying to update timesheetLine of employee {} on date {} with workOrder {} and hours {} ", name, loggedHoursByDay.date(), workOrder.value(), loggedHoursByDay.hours());
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
