package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;

public class OtherSyncError extends SyncRecord {
    public OtherSyncError(EmployeeIdentification employerData, RecordData recordData) {
        super(employerData, recordData);
        this.errorCode = 4;
    }
}
