package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;



public class HoursNotEqualSyncError extends SyncRecord {
    public HoursNotEqualSyncError(EmployeeData employerData, RecordData recordData) {
        super(employerData, recordData);
        this.errorCode = 2;
    }
}
