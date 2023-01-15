package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.timesheet.TimesheetService;

public class ErrorCommand implements SyncCommand{
    private final String errorMessage;

    public ErrorCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public void execute(TimesheetService timesheetService) {
        throw new IllegalStateException("This should never have been executed");
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
