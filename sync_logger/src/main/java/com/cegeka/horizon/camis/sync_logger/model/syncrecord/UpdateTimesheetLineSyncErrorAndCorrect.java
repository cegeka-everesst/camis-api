package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;


public class UpdateTimesheetLineSyncErrorAndCorrect extends SyncRecord {
    public UpdateTimesheetLineSyncErrorAndCorrect(EmployeeIdentification employerData, RecordData recordData, double hoursLogged) {
        super(employerData, recordData);
        this.hoursLogged = hoursLogged;
    }
}
