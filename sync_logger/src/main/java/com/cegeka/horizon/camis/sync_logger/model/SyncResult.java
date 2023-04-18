package com.cegeka.horizon.camis.sync_logger.model;

import java.util.ArrayList;
import java.util.List;

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
}
