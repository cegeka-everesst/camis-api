package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.sync_logger.model.syncrecord.SyncRecord;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

public interface SyncCommand {
    SyncRecord execute(WebClient webClient, TimesheetService timesheetService, SyncLoggerService syncLoggerService);

    default boolean isError() {
        return false;
    }
}
