package com.cegeka.horizon.camis.workorder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;

public class WorkOrderAccess {
    private ResourceId resourceId;
    private WorkOrder workorder;
    private boolean access;

    public WorkOrderAccess(ResourceId resourceId, WorkOrder workorder, boolean access) {
        this.resourceId = resourceId;
        this.workorder = workorder;
        this.access = access;
    }

    @Override
    public String toString() {
        return "WorkOrderAccess{" + "resourceId=" + resourceId +
                ", workorder=" + workorder +
                ", access=" + access +
                '}';
    }
}
