package com.cegeka.horizon.camis.timesheet.api.get;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimesheetEntryItem {
    @JsonProperty("Identifier")
    String identifier;

    @JsonProperty("Status")
    String status;

    @JsonProperty("Description")
    String description;

    @JsonProperty("Project")
    String project;

    @JsonProperty("TimeCode")
    String timeCode;

    @JsonProperty("WorkOrder")
    String workOrder;

    @JsonProperty("Unit")
    String unit;

    @JsonProperty("WorkDayList")
    WorkDayList workDayList;

    @JsonProperty("Activity")
    String activity;


    @Override
    public String toString() {
        return "TimesheetEntryItem{" + "identifier='" + identifier + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", project='" + project + '\'' +
                ", timeCode='" + timeCode + '\'' +
                ", workOrder='" + workOrder + '\'' +
                ", unit='" + unit + '\'' +
                ", workdays=" + workDayList +
                '}';
    }
}
