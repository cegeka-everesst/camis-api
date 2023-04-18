package com.cegeka.horizon.camis.sync_logger.model;

public abstract class SyncRecord {
    private final String message;
    private final String employeeName;

    protected SyncRecord(String message, String employeeName) {
        this.message = message;
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "Message: " + this.message + "\n" + "Employee name : " + this.employeeName + "\n";
    }
}
