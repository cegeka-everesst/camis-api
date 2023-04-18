package com.cegeka.horizon.camis.sync_logger.records;

public abstract class SyncRecord {
    private final String message;
    private final String employeeName;

    protected SyncRecord(String message, String employeeName) {
        this.message = message;
        this.employeeName = employeeName;
    }
}
