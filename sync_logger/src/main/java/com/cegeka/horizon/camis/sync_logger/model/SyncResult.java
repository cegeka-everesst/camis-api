package com.cegeka.horizon.camis.sync_logger.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SyncResult {
    private List<SyncRecord> syncRecords;

    public SyncResult() {
        this.syncRecords = new ArrayList<>();
    }

    public void addSyncRecord(SyncRecord syncRecord) {
        syncRecords.add(syncRecord);
    }

    public List<SyncRecord> getSyncRecords() {
        return syncRecords;
    }

    public JSONObject getSyncRecordsToJson() {
        JSONObject total = new JSONObject();
        if (this.syncRecords != null) {
            int count = 0;
            for (SyncRecord syncRecord : this.syncRecords) {
                total.put(String.valueOf(count), syncRecord.toJson());
                count++;
            }
        }
        return total;
    }
}
