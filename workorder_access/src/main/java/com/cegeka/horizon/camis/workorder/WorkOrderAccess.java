package com.cegeka.horizon.camis.workorder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.Workorder;

public class WorkOrderAccess {
    private ResourceId resourceId;
    private Workorder workorder;
    private boolean access;

    public WorkOrderAccess(ResourceId resourceId, Workorder workorder, boolean access) {
        this.resourceId = resourceId;
        this.workorder = workorder;
        this.access = access;
    }
}
