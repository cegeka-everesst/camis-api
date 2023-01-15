package com.cegeka.horizon.camis.synctimesheet.service.command;

import com.cegeka.horizon.camis.timesheet.TimesheetService;

public interface SyncCommand {
    void execute(TimesheetService timesheetService);

    default boolean isError() {return false;}
}
