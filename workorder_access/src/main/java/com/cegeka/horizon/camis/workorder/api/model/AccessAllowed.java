package com.cegeka.horizon.camis.workorder.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessAllowed {
    @JsonProperty("accessAllowed")
    boolean accessAllowed;

    public boolean isAccessAllowed() {
        return accessAllowed;
    }
}
