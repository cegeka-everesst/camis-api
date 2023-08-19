package com.cegeka.horizon.camis.sync.logger.model.result;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;

public record OtherSyncError(EmployeeIdentification employee, CamisWorkorderInfo recordData) implements  SyncResult{
    @Override
    public SyncResultType type() {
        return SyncResultType.ERROR_OTHER;
    }
}
