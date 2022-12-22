package com.cegeka.horizon.camis.timesheet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Timesheet {
    @JsonProperty("EntryList")
    private TimesheetEntryList timesheetEntryList;

    @Override
    public String toString() {
        return "Timesheet{" + "timesheetEntryList=" + timesheetEntryList +
                '}';
    }
}
