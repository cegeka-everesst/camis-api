package com.cegeka.horizon.camis.sync_logger.model;

import java.time.LocalDate;

import org.json.JSONObject;

public class HoursNotEqualSyncError extends SyncRecord {
    private final LocalDate startDate;

    public HoursNotEqualSyncError(String message, String employeeName, LocalDate date) {
        super(message, employeeName);
        this.startDate = date;
    }

    @Override
    public String toString() {
        return super.toString() + "Start date: " + this.startDate + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject superObject = super.toJson();
        superObject.put("startDate", startDate);
        return superObject;
    }
}
