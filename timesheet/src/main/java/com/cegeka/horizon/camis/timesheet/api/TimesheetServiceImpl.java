package com.cegeka.horizon.camis.timesheet.api;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.*;
import com.cegeka.horizon.camis.timesheet.api.delete.StatusResponseHolder;
import com.cegeka.horizon.camis.timesheet.api.get.LocalDateRangeSplitter;
import com.cegeka.horizon.camis.timesheet.api.post.CreateTimesheetEntry;
import com.cegeka.horizon.camis.timesheet.api.get.EmployeeMapper;
import com.cegeka.horizon.camis.timesheet.api.get.Timesheet;
import com.cegeka.horizon.camis.timesheet.api.post.CreateTimesheetEntryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.threeten.extra.LocalDateRange;
import reactor.core.publisher.Mono;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class TimesheetServiceImpl implements TimesheetService {

    @Autowired
    private WebClient webClient;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * Get the timesheet entries of a specified dateRange.
     *
     * Beware : The API seems only able to retrieve multiple weeks if the dateRange is split into weeks, otherwise only the first week is retrieved.
     * So we'll need to split this here.
     * @param resourceId
     * @param employeeName
     * @param dateRange
     * @return
     */
    @Override
    public Employee getTimesheetEntries(ResourceId resourceId, String employeeName, LocalDateRange dateRange) {
        return LocalDateRangeSplitter.splitByWeek(dateRange).stream().map(
          range -> employeeMapper.map(
                  webClient.get()
                  .uri(uriBuilder -> uriBuilder.path("timesheet")
                          .queryParam("resourceId", resourceId.value())
                          .queryParam("dateFrom", range.getStart().format(ofPattern("yyyy-MM-dd")))
                          .queryParam("dateTo", range.getEnd().format(ofPattern("yyyy-MM-dd"))).build())
                  .retrieve()
                  .bodyToMono(Timesheet.class)
                  .block(), resourceId, employeeName)
        ).reduce(new Employee(resourceId, employeeName), new Employee.MergeEmployeesOperator());

    }

    @Override
    public TimesheetLineIdentifier createTimesheetEntry(ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHours) {
        CreateTimesheetEntry createTimesheetEntry = createTimesheetEntryRequest(resourceId, workOrder, loggedHours);

        return mapResult(webClient.post()
                .uri(uriBuilder -> uriBuilder.path("timesheet").build())
                .body(Mono.just(createTimesheetEntry), CreateTimesheetEntry.class)
                .retrieve()
                .bodyToMono(CreateTimesheetEntryResult.class)
                .block());
    }

    @Override
    public TimesheetLineIdentifier updateTimesheetEntry(TimesheetLineIdentifier timesheetLineIdentifier, ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHours) {
        CreateTimesheetEntry createTimesheetEntry = createTimesheetEntryRequest(resourceId, workOrder, loggedHours);

        return mapResult(webClient.put()
                .uri(uriBuilder -> uriBuilder.path("timesheet")
                        .queryParam("identifier", timesheetLineIdentifier.value())
                        .build())
                .body(Mono.just(createTimesheetEntry), CreateTimesheetEntry.class)
                .retrieve()
                .bodyToMono(CreateTimesheetEntryResult.class)
                .block());
    }

    @Override
    public boolean deleteTimesheetEntry(TimesheetLineIdentifier timesheetLineIdentifier, ResourceId resourceId) {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path("timesheet")
                        .queryParam("identifier", timesheetLineIdentifier.value())
                        .queryParam("resourceId", resourceId.value())
                        .build())
                .retrieve()
                .bodyToMono(StatusResponseHolder.class)
                .block().isOk();
    }

    private CreateTimesheetEntry createTimesheetEntryRequest(ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay) {
        return new CreateTimesheetEntry(
                resourceId.value(),
                Status.DRAFT.value(),
                "",
                CreateTimesheetEntry.DEFAULT_WORKORDER_DESCRIPTION,
                CreateTimesheetEntry.DEFAULT_WORKORDER_PROJECT,
                TimeCode.WORK_DAY.value(),
                workOrder.value(),
                CreateTimesheetEntry.DEFAULT_EXTERNAL_REF,
                loggedHoursByDay.date().format(ofPattern("yyyy-MM-dd")) + "T00:00:00",
                Double.toString(loggedHoursByDay.hours()),
                CreateTimesheetEntry.DEFAULT_INV_VALUE
        );
    }

    private TimesheetLineIdentifier mapResult(CreateTimesheetEntryResult timesheet) {
        if(! timesheet.isOk()) return TimesheetLineIdentifier.ERROR_OCCURRED;
        return new TimesheetLineIdentifier(timesheet.getIdentifier());
    }
}
