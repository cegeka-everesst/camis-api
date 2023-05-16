package com.cegeka.horizon.camis.sync_logger.model;

import com.cegeka.horizon.camis.sync_logger.model.syncrecord.SyncRecord;

import java.util.ArrayList;
import java.util.List;


public class SyncResult {
    private final List<SyncDay> syncDays;
    private final List<SyncRecord> syncRecords;

    public SyncResult() {
        this.syncDays = new ArrayList<>();
        this.syncRecords = new ArrayList<>();
    }

    public void addSyncRecord(SyncRecord syncRecord) {
        syncRecords.add(syncRecord);
    }
    public void addSyncDays(List<SyncDay> syncDays) {
        this.syncDays.addAll(syncDays);
    }

    public List<SyncDay> getSyncDays() {
        return syncDays;
    }

    public List<SyncRecord> getSyncRecords() {
        return syncRecords;
    }
}
