package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;


public record HoursNotEqualSyncError(EmployeeIdentification employee, CamisWorkorderInfo camisWorkorderInfo) implements SyncResult {
    @Override
    public SyncResultType type() {
        return SyncResultType.ERROR_HOURS_NOT_EQUAL;
    }
}
