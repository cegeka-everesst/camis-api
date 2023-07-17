package com.cegeka.horizon.camis.sync_logger.model.data;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public final class RecordData {
    private final LocalDate date;
    private final String message;
    private final WorkOrder workOrder;

    public RecordData(LocalDate localDate, String message, WorkOrder workOrder) {
        this.date = localDate;
        this.message = message;
        this.workOrder = workOrder;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getWorkOrder() {
        if (workOrder == null){
            return "";
        }
        return workOrder.value();
    }
}

