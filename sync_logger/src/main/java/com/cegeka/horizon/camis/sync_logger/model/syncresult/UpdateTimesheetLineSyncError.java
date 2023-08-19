package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;


public record UpdateTimesheetLineSyncError(EmployeeIdentification employee, CamisWorkorderInfo camisWorkorderInfo, double hoursLogged) implements SyncResult {
    @Override
    public SyncResultType type() {
        return SyncResultType.ERROR_UNABLE_UPDATE_TIMESHEET_LINE;
    }
}
