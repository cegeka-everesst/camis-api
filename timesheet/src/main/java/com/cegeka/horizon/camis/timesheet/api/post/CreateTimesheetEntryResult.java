package com.cegeka.horizon.camis.timesheet.api.post;

import com.cegeka.horizon.camis.timesheet.api.get.WorkDayList;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateTimesheetEntryResult {
    @JsonProperty("Identifier")
    String identifier;

    @JsonProperty("Description")
    String description;

    @JsonProperty("TimeCode")
    String timeCode;

    @JsonProperty("WorkOrder")
    String workOrder;

    @JsonProperty("ExternalRef")
    String externalRef;

    @JsonProperty("WorkDayList")
    WorkDayList workDayList;

    @JsonProperty("Response")
    List<StatusResponse> response;

    public boolean isOk(){
        return response.stream().allMatch(StatusResponse::isOk);
    }

    @Override
    public String toString() {
        return "TimesheetEntryItem{" + "identifier='" + identifier + '\'' +
                ", description='" + description + '\'' +
                ", timeCode='" + timeCode + '\'' +
                ", workOrder='" + workOrder + '\'' +
                ", workdays=" + workDayList +
                ", response=" + response +
                '}';
    }


    public String getIdentifier() {
        return identifier;
    }
}
