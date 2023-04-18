package com.cegeka.horizon.camis.sync_logger.model;

import java.time.LocalDate;

public class HoursMinimumSyncError extends SyncRecord {
    private final double hoursLogged;
    private final LocalDate date;

    public HoursMinimumSyncError(String message, String employeeName, LocalDate date, double hoursLogged) {
        super(message, employeeName);
        this.hoursLogged = hoursLogged;
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() + "Minimum hours logged: " + this.hoursLogged + "\n" + "Start date: " + this.date + "\n";
    }
}
