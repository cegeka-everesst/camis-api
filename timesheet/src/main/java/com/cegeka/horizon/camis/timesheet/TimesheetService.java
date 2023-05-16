package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import org.springframework.web.reactive.function.client.WebClient;
import org.threeten.extra.LocalDateRange;

import java.util.Optional;

public interface TimesheetService {
    Optional<WeeklyTimesheet> getTimesheetEntries(WebClient webClient, ResourceId resourceId, String employeeName, LocalDateRange dateRange);

    TimesheetLineIdentifier createTimesheetEntry(WebClient webClient, ResourceId resourceId, TimeCode timeCode, WorkOrder workOrder, LoggedHoursByDay loggedHours);

    TimesheetLineIdentifier updateTimesheetEntry(WebClient webClient, TimesheetLineIdentifier timesheetLineIdentifier, ResourceId resourceId, TimeCode timeCode, WorkOrder workOrder, LoggedHoursByDay loggedHours);

    boolean deleteTimesheetEntry(WebClient webClient, TimesheetLineIdentifier timesheetLineIdentifier, ResourceId resourceId);

}
