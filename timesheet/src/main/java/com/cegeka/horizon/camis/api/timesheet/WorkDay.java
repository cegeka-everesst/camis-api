package com.cegeka.horizon.camis.api.timesheet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkDay {
    @JsonProperty("Day")
    private String day;

    @JsonProperty("HoursWorked")
    private Double hoursWorked;

    @Override
    public String toString() {
        return "WorkDay{" + "day='" + day + '\'' +
                ", hoursWorked=" + hoursWorked +
                '}';
    }
}
