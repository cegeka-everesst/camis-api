package com.cegeka.horizon.camis.api.timesheet;

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
