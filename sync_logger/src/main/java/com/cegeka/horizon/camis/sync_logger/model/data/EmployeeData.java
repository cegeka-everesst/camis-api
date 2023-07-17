package com.cegeka.horizon.camis.sync_logger.model.data;

import com.cegeka.horizon.camis.domain.ResourceId;

public final class EmployeeData {
    private final ResourceId resourceId;
    private final String employeeName;

    private final String slackEmployeeName;

    public EmployeeData(ResourceId resourceId, String employeeName, String slackEmployeeName) {
        this.resourceId = resourceId;
        this.employeeName = employeeName;
        this.slackEmployeeName = slackEmployeeName;
    }

    public ResourceId getResourceId() {
        return resourceId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getSlackEmployeeName() {
        return slackEmployeeName;
    }
}
