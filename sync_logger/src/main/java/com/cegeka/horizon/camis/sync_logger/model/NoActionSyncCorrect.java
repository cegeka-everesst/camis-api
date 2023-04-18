package com.cegeka.horizon.camis.sync_logger.model;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

import org.json.JSONObject;

public class NoActionSyncCorrect extends SyncRecord {
    private final WorkOrder workOrder;
    private final LocalDate startDate;

    public NoActionSyncCorrect(String message, String employeeName, LocalDate date, WorkOrder workOrder) {
        super(message, employeeName);
        this.workOrder = workOrder;
        this.startDate = date;
    }

    @Override
    public String toString() {
        return super.toString() + "Start date: " + this.startDate + "\n" + "Work order: " + this.workOrder.value() + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject superObject = super.toJson();
        superObject.put("startDate", startDate);
        superObject.put("workOrder", workOrder.value());
        return superObject;
    }
}
