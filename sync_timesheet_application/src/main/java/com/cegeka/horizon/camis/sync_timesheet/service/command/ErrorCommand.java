package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.sync_logger.model.syncrecord.SyncRecord;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class ErrorCommand implements SyncCommand {
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");
    private final String errorMessage;

    public ErrorCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public SyncRecord execute(WebClient webClient, TimesheetService timesheetService, SyncLoggerService syncLoggerService) {
        logger.error("Error command should never have been executed");
        throw new IllegalStateException("This should never have been executed");
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
