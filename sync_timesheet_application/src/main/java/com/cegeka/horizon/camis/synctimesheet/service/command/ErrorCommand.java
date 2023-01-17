package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorCommand implements SyncCommand{
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
    public void execute(TimesheetService timesheetService) {
        logger.error("Error command should never have been executed");
        throw new IllegalStateException("This should never have been executed");
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
