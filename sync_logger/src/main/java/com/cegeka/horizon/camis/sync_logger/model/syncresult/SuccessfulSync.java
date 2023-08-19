package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public record SuccessfulSync(EmployeeIdentification employee, LocalDate date, WorkOrder workOrder, double hoursLoggedCamis, double hoursLoggedTempo) implements SyncResult {
    @Override
    public SyncResultType type() {
        return SyncResultType.SUCCESSFUL_SYNC;
    }
}
