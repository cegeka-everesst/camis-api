package com.cegeka.horizon.camis.sync_logger.model.syncrecord;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;

public abstract class SyncRecord {

    private final EmployeeIdentification employeeIdentification;
    private final RecordData recordData;
    protected int errorCode;
    protected double hoursLogged;

    protected SyncRecord(EmployeeIdentification employeeIdentification, RecordData recordData) {
        this.employeeIdentification = employeeIdentification;
        this.recordData = recordData;
        this.errorCode = -1;
        this.hoursLogged = -1;
    }

    public EmployeeIdentification employeeIdentification() {
        return employeeIdentification;
    }

    public RecordData recordData() {
        return recordData;
    }

    public int errorCode() {
        return errorCode;
    }

    public double hoursLogged() {
        return hoursLogged;
    }
}
