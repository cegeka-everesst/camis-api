package com.cegeka.horizon.camis.sync.logger.model.result;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;

public record NoActionSyncCorrect(EmployeeIdentification employee, CamisWorkorderInfo camisWorkorderInfo) implements SyncResult {
    @Override
    public SyncResultType type() {
        return SyncResultType.NO_SYNC_ACTION_REQUIRED;
    }
}
