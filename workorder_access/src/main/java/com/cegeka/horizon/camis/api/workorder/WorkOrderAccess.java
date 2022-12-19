package com.cegeka.horizon.camis.api.workorder;


import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkOrderAccess {
    @JsonProperty("AccessAllowed")
    boolean accessAllowed;

    public boolean isAccessAllowed() {
        return accessAllowed;
    }
}
