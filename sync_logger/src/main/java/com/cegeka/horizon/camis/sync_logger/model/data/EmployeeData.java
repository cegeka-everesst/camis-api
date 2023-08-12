package com.cegeka.horizon.camis.sync_logger.model.data;

import com.cegeka.horizon.camis.domain.ResourceId;

public final class EmployeeData {
    private final ResourceId resourceId;
    private final String employeeName;


    public EmployeeData(ResourceId resourceId, String employeeName) {
        this.resourceId = resourceId;
        this.employeeName = employeeName;
    }

    public ResourceId getResourceId() {
        return resourceId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

}
