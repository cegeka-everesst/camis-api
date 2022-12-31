package com.cegeka.horizon.camis.timesheet.api.get;

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
