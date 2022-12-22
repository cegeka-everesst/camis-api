package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;

import java.time.LocalDate;

public interface TimesheetService {
    String getTimesheetEntries(ResourceId resourceId, LocalDate dateFrom, LocalDate dateTo);
}
