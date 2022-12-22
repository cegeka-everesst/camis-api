package com.cegeka.horizon.camis.workorder.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessAllowed {
    @JsonProperty("accessAllowed")
    boolean AccessAllowed;

    public boolean isAccessAllowed() {
        return AccessAllowed;
    }
}
