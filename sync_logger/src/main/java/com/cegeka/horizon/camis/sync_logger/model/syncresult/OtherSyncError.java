package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;

public record OtherSyncError(EmployeeIdentification employee, CamisWorkorderInfo recordData) implements  SyncResult{
    @Override
    public SyncResultType type() {
        return SyncResultType.ERROR_OTHER;
    }
}
