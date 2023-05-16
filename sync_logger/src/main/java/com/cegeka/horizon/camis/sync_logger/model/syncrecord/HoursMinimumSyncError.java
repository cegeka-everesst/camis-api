package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;


public class HoursMinimumSyncError extends SyncRecord {

    public HoursMinimumSyncError(EmployeeData employerData, RecordData recordData, double hoursLogged) {
        super(employerData, recordData);
        this.errorCode = 1;
        this.hoursLogged = hoursLogged;
    }
}
