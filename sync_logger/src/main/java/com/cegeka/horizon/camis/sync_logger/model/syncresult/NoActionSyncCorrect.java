package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;

public record NoActionSyncCorrect(EmployeeIdentification employee, CamisWorkorderInfo camisWorkorderInfo) implements SyncResult {
    @Override
    public SyncResultType type() {
        return SyncResultType.NO_SYNC_ACTION_REQUIRED;
    }
}
