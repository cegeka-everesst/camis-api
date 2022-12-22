package com.cegeka.horizon.camis.workorder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public interface WorkorderAccessService {
    WorkOrderAccess isAccessAllowed(ResourceId resourceId, WorkOrder workorder, LocalDate localDate);
}
