package com.cegeka.horizon.camis.sync_logger.model;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

import org.json.JSONObject;

public class UpdateTimsheetLineSyncErrorAndCorrect extends SyncRecord {
    private final LocalDate startDate;
    private final double hoursLogged;
    private final WorkOrder workOrder;

    public UpdateTimsheetLineSyncErrorAndCorrect(String message, String employeeName, LocalDate date, double hoursLogged, WorkOrder workOrder) {
        super(message, employeeName);
        this.startDate = date;
        this.hoursLogged = hoursLogged;
        this.workOrder = workOrder;
    }

    @Override
    public String toString() {
        return super.toString() + "Start date: " + this.startDate + "\n" + "Hours logged: " + this.hoursLogged + "\n" + "Work order: " + this.workOrder.value() + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject superObject = super.toJson();
        superObject.put("startDate", startDate);
        superObject.put("hoursLogged", hoursLogged);
        superObject.put("workOrder", workOrder.value());
        return superObject;
    }
}
