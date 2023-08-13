package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;

public abstract class SyncRecord {

    private final EmployeeIdentification employerData;
    private final RecordData recordData;
    protected int errorCode;
    protected double hoursLogged;

    protected SyncRecord(EmployeeIdentification employerData, RecordData recordData) {
        this.employerData = employerData;
        this.recordData = recordData;
        this.errorCode = -1;
        this.hoursLogged = -1;
    }

    public EmployeeIdentification getEmployerData() {
        return employerData;
    }

    public RecordData getRecordData() {
        return recordData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public double getHoursLogged() {
        return hoursLogged;
    }
}
