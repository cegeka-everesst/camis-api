package com.cegeka.horizon.camis.sync.logger.model.result;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;


public interface SyncResult {
    EmployeeIdentification employee();
    SyncResultType type();
}
