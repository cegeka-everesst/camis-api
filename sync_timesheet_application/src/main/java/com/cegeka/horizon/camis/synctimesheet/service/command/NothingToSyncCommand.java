package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync_logger.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.TimesheetService;

import java.time.LocalDate;

public class NothingToSyncCommand implements SyncCommand{
    private final String name;
    private final WorkOrder workOrder;
    private final LocalDate date;

    public NothingToSyncCommand(String name, WorkOrder workOrder, LocalDate date) {
        this.name = name;
        this.workOrder = workOrder;
        this.date = date;
    }

    @Override
    public void execute(TimesheetService timesheetService, SyncLoggerService syncLoggerService) {
        syncLoggerService.logAndAddSyncRecord("No Sync Action needed for " + name + " on workOrder " + workOrder.value() + " and date " + date, name, date, workOrder);
    }

}
