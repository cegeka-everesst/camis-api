package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;


public record HoursMinimumSyncError(EmployeeIdentification employee, CamisWorkorderInfo camisWorkorderInfo, double minimumHoursRequired) implements SyncResult {
    @Override
    public SyncResultType type() {
        return SyncResultType.ERROR_INSUFFICIENT_HOURS;
    }
}
