package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import org.threeten.extra.LocalDateRange;

import java.util.Optional;

public interface TimesheetService {
    Optional<WeeklyTimesheet> getTimesheetEntries(ResourceId resourceId, String employeeName, LocalDateRange dateRange);

    TimesheetLineIdentifier createTimesheetEntry(ResourceId resourceId, TimeCode timeCode, WorkOrder workOrder, LoggedHoursByDay loggedHours);

    TimesheetLineIdentifier updateTimesheetEntry(TimesheetLineIdentifier timesheetLineIdentifier, ResourceId resourceId, TimeCode timeCode, WorkOrder workOrder, LoggedHoursByDay loggedHours);

    boolean deleteTimesheetEntry(TimesheetLineIdentifier timesheetLineIdentifier, ResourceId resourceId);

}
