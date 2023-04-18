package com.cegeka.horizon.camis.sync_logger.records;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public class NoActionSyncCorrect extends SyncRecord {
    private final WorkOrder workOrder;
    private final LocalDate date;

    public NoActionSyncCorrect(String message, String employeeName, LocalDate date, WorkOrder workOrder) {
        super(message, employeeName);
        this.workOrder = workOrder;
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Start date: " + this.date + "\n" +
                "Work order: " + this.workOrder.value() + "\n";
    }
}
