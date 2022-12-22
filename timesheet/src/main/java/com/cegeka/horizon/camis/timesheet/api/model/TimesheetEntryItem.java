package com.cegeka.horizon.camis.timesheet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimesheetEntryItem {
    @JsonProperty("Identifier")
    private String identifier;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Project")
    private String project;

    @JsonProperty("TimeCode")
    private String timeCode;

    @JsonProperty("WorkOrder")
    private String workOrder;

    @JsonProperty("Unit")
    private String unit;

    @JsonProperty("WorkDayList")
    private WorkDayList workDayList;


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
