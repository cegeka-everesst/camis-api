package com.cegeka.horizon.camis.sync_error_logger;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyncErrorTable {
    private final UUID syncTableUUID;
    private List<SyncErrorRecord> syncErrorRecords;

    public SyncErrorTable(UUID syncTableUUID) {
        this.syncTableUUID = syncTableUUID;
        this.syncErrorRecords = new ArrayList<>();
    }

    public void addSyncErrorRecord(String message, String employer) {
        syncErrorRecords.add(new SyncErrorRecord(message, employer));
    }

    public void addSyncErrorRecord(String message, String employer, LocalDate date, double hours) {
        syncErrorRecords.add(new SyncErrorRecord(message, employer, date, hours));
    }

    public void addSyncErrorRecord(String message, String employer, LocalDate date, WorkOrder workOrder) {
        syncErrorRecords.add(new SyncErrorRecord(message, employer, date, workOrder));
    }

    public void addSyncErrorRecord(String message, String employer, LocalDate date, double hours, WorkOrder workOrder) {
        syncErrorRecords.add(new SyncErrorRecord(message, employer, date, hours, workOrder));
    }

}
