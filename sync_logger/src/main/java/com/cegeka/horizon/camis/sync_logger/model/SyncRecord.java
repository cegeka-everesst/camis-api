package com.cegeka.horizon.camis.sync_logger.model;

import org.json.JSONObject;

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

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        jsonObject.put("employeeName", employeeName);
        return jsonObject;
    }
}
