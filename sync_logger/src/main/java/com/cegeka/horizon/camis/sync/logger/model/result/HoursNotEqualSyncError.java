package com.cegeka.horizon.camis.sync.logger.model.result;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;


public record HoursNotEqualSyncError(EmployeeIdentification employee, CamisWorkorderInfo camisWorkorderInfo) implements SyncResult {
    @Override
    public SyncResultType type() {
        return SyncResultType.ERROR_HOURS_NOT_EQUAL;
    }
}
