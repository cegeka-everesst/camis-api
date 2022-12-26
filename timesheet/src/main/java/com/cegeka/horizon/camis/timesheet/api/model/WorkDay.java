package com.cegeka.horizon.camis.timesheet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkDay {
    @JsonProperty("Day")
    String day;

    @JsonProperty("HoursWorked")
    Double hoursWorked;

    @Override
    public String toString() {
        return "WorkDay{" + "day='" + day + '\'' +
                ", hoursWorked=" + hoursWorked +
                '}';
    }
}
