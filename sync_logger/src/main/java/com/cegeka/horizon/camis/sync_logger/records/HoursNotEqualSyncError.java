package com.cegeka.horizon.camis.sync_logger.records;

import java.time.LocalDate;

public class HoursNotEqualSyncError extends SyncRecord {
    private final LocalDate date;

    public HoursNotEqualSyncError(String message, String employeeName, LocalDate date) {
        super(message, employeeName);
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Start date: " + this.date + "\n";
    }
}
