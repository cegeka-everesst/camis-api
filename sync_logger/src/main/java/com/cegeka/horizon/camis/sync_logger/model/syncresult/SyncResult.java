package com.cegeka.horizon.camis.sync_logger.model.syncresult;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.domain.ResourceId;

import java.time.LocalDate;


public interface SyncResult {
    EmployeeIdentification employee();
    SyncResultType type();
}
