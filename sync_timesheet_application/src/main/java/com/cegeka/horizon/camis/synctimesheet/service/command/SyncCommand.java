package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.TimesheetService;

public interface SyncCommand {
    void execute(TimesheetService timesheetService, SyncLoggerService syncLoggerService);

    default boolean isError() {
        return false;
    }
}
