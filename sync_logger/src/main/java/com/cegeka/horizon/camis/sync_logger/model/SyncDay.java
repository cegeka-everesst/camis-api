package com.cegeka.horizon.camis.sync_logger.model;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public class SyncDay {
    private final ResourceId resourceId;
    private final LocalDate date;
    private final WorkOrder workOrder;
    private final double hoursLoggedCamis;
    private final double hoursLoggedTempo;

    public SyncDay(ResourceId resourceId, LocalDate date, WorkOrder workOrder, double hoursLoggedCamis, double hoursLoggedTempo) {
        this.resourceId = resourceId;
        this.date = date;
        this.workOrder = workOrder;
        this.hoursLoggedCamis = hoursLoggedCamis;
        this.hoursLoggedTempo = hoursLoggedTempo;
    }

    public ResourceId getResourceId() {
        return resourceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public double getHoursLoggedCamis() {
        return hoursLoggedCamis;
    }

    public double getHoursLoggedTempo() {
        return hoursLoggedTempo;
    }
}
