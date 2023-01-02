package com.cegeka.horizon.camis.timesheet.api.delete;

import com.cegeka.horizon.camis.timesheet.api.post.StatusResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResponseHolder {
    @JsonProperty("Response")
    StatusResponse response;

    public boolean isOk() {
        return response.isOk();
    }
}
