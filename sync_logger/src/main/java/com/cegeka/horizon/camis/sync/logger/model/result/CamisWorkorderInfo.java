package com.cegeka.horizon.camis.sync.logger.model.result;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public record CamisWorkorderInfo(LocalDate date, String message, WorkOrder workOrder){
}

