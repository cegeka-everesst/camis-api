package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class NothingToSyncCommand implements SyncCommand{
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");
    private final String name;
    private final WorkOrder workOrder;
    private final LocalDate date;

    public NothingToSyncCommand(String name, WorkOrder workOrder, LocalDate date) {
        this.name = name;
        this.workOrder = workOrder;
        this.date = date;
    }

    @Override
    public void execute(TimesheetService timesheetService) {
        logger.info("No Sync Action needed for {} on workOrder {} and date {}", name, workOrder.value(), date);
    }

}
