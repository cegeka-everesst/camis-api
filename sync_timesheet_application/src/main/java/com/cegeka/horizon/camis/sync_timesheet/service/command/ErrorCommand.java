package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

public class ErrorCommand implements SyncCommand {
    private final String errorMessage;

    public ErrorCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public SyncResult execute(WebClient webClient, TimesheetService timesheetService) {
        throw new IllegalStateException("This should never have been executed");
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
