package com.cegeka.horizon.camis.sync_error_logger;

import java.time.LocalDate;

import com.cegeka.horizon.camis.domain.WorkOrder;

public class SyncErrorRecord {
    private final String message;
    private final String employeeName;
    private final LocalDate date;
    private final double hoursLogged;
    private final WorkOrder workOrder;

    public SyncErrorRecord(String message, String employeeName) {
        this.message = message;
        this.employeeName = employeeName;
        this.date = null;
        this.hoursLogged = -1;
        this.workOrder = null;
    }

    public SyncErrorRecord(String message, String employeeName, LocalDate date, WorkOrder workOrder) {
        this.message = message;
        this.employeeName = employeeName;
        this.date = date;
        this.hoursLogged = -1;
        this.workOrder = workOrder;
    }

    public SyncErrorRecord(String message, String employeeName, LocalDate date, double hoursLogged) {
        this.message = message;
        this.employeeName = employeeName;
        this.date = date;
        this.hoursLogged = hoursLogged;
        this.workOrder = null;
    }

    public SyncErrorRecord(String message, String employeeName, LocalDate date, double hoursLogged, WorkOrder workOrder) {
        this.message = message;
        this.employeeName = employeeName;
        this.date = date;
        this.hoursLogged = hoursLogged;
        this.workOrder = workOrder;
    }
}
