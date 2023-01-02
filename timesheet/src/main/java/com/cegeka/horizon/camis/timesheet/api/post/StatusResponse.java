package com.cegeka.horizon.camis.timesheet.api.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResponse {
    @JsonProperty("Status")
    String status;

    @JsonProperty("ReturnCode")
    String returnCode;

    @Override
    public String toString() {
        return "StatusResponse{" +
                "status='" + status + '\'' +
                ", returnCode='" + returnCode + '\'' +
                '}';
    }


    public boolean isOk() {
        return status.equals("OK");
    }
}
