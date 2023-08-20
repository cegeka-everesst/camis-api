package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

public interface SyncCommand {
    SyncResult execute(WebClient webClient, TimesheetService timesheetService);

    default boolean isError() {
        return false;
    }
}
