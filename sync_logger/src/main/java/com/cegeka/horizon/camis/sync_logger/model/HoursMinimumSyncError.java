package com.cegeka.horizon.camis.sync_logger.model;

import java.time.LocalDate;

import org.json.JSONObject;


public class HoursMinimumSyncError extends SyncRecord {
    private final double hoursLogged;
    private final LocalDate startDate;

    public HoursMinimumSyncError(String message, String employeeName, LocalDate date, double hoursLogged) {
        super(message, employeeName);
        this.hoursLogged = hoursLogged;
        this.startDate = date;
    }

    @Override
    public String toString() {
        return super.toString() + "Minimum hours logged: " + this.hoursLogged + "\n" + "Start date: " + this.startDate + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject superObject = super.toJson();
        superObject.put("hoursLogged", hoursLogged);
        superObject.put("startDate", startDate);
        return superObject;
    }
}
