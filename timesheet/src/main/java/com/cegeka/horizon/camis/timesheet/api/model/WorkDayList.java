package com.cegeka.horizon.camis.timesheet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WorkDayList {
    @JsonProperty("WorkDay")
    private List<WorkDay> workdays;

    @Override
    public String toString() {
        return "WorkDayList{" + "workdays=" + workdays +
                '}';
    }
}
