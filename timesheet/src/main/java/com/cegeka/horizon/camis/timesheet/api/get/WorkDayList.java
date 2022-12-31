package com.cegeka.horizon.camis.timesheet.api.get;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WorkDayList {
    @JsonProperty("WorkDay")
    List<WorkDay> workdays;

    @Override
    public String toString() {
        return "WorkDayList{" + "workdays=" + workdays +
                '}';
    }
}
