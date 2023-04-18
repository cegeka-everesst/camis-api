package com.cegeka.horizon.camis.sync_logger;

import com.cegeka.horizon.camis.sync_logger.records.*;

import java.util.ArrayList;
import java.util.List;

public class SyncList {
    private List<SyncRecord> syncRecords;

    public SyncList() {
        this.syncRecords = new ArrayList<>();
    }

    public void addSyncRecord(SyncRecord syncRecord) {
        syncRecords.add(syncRecord);
    }

    public List<SyncRecord> getSyncRecords() {
        return syncRecords;
    }
}
