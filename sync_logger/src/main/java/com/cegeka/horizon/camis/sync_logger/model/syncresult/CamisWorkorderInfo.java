package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public record CamisWorkorderInfo(LocalDate date, String message, WorkOrder workOrder){
}

