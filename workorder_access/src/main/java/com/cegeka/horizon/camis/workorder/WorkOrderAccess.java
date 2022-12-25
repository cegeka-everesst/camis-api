package com.cegeka.horizon.camis.workorder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;

public record WorkOrderAccess (ResourceId resourceId, WorkOrder workOrder, boolean access) {
}
