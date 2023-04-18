package com.cegeka.horizon.camis.sync_logger.model;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public class UpdateTimsheetLineSyncErrorAndCorrect extends SyncRecord {
    private final LocalDate date;
    private final double hoursLogged;
    private final WorkOrder workOrder;

    public UpdateTimsheetLineSyncErrorAndCorrect(String message, String employeeName, LocalDate date, double hoursLogged, WorkOrder workOrder) {
        super(message, employeeName);
        this.date = date;
        this.hoursLogged = hoursLogged;
        this.workOrder = workOrder;
    }

    @Override
    public String toString() {
        return super.toString() + "Start date: " + this.date + "\n" + "Hours logged: " + this.hoursLogged + "\n" + "Work order: " + this.workOrder.value() + "\n";
    }
}
