package com.cegeka.horizon.camis.api.timesheet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TimesheetEntryList {
    @JsonProperty("Entry")
    private List<TimesheetEntryItem> entry;

    @Override
    public String toString() {
        return "TimesheetEntryList{" + "entry=" + entry +
                '}';
    }
}
