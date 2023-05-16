package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;


public class UpdateTimesheetLineSyncErrorAndCorrect extends SyncRecord {
    public UpdateTimesheetLineSyncErrorAndCorrect(EmployeeData employerData, RecordData recordData, double hoursLogged) {
        super(employerData, recordData);
        this.hoursLogged = hoursLogged;
    }
}
