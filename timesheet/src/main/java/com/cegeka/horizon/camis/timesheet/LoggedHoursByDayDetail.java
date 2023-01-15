package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.WorkOrder;

public record LoggedHoursByDayDetail(TimesheetLineIdentifier identifier, Status status, String description, TimeCode timeCode, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay) {

    public boolean canBeDeleted() {
        return status.canBeDeleted();
    }
}
