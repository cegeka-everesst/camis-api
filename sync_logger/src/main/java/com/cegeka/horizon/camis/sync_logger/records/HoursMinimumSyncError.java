package com.cegeka.horizon.camis.sync_logger.records;

import java.time.LocalDate;

public class HoursMinimumSyncError extends SyncRecord {
    private final double hoursLogged;
    private final LocalDate date;

    public HoursMinimumSyncError(String message, String employeeName, LocalDate date, double hoursLogged) {
        super(message, employeeName);
        this.hoursLogged = hoursLogged;
        this.date = date;
    }
}
