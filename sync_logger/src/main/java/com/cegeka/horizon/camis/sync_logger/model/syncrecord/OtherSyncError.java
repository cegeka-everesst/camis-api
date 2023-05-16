package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;

public class OtherSyncError extends SyncRecord {
    public OtherSyncError(EmployeeData employerData, RecordData recordData) {
        super(employerData, recordData);
        this.errorCode = 4;
    }
}
