package com.cegeka.horizon.camis.timesheet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TimesheetEntryList {
    @JsonProperty("Entry")
    List<TimesheetEntryItem> entries;

    @Override
    public String toString() {
        return "TimesheetEntryList{" + "entry=" + entries +
                '}';
    }
}
