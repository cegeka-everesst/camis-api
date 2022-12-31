package com.cegeka.horizon.camis.timesheet.api;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.*;
import com.cegeka.horizon.camis.timesheet.api.post.CreateTimesheetEntry;
import com.cegeka.horizon.camis.timesheet.api.get.EmployeeMapper;
import com.cegeka.horizon.camis.timesheet.api.get.Timesheet;
import com.cegeka.horizon.camis.timesheet.api.post.CreateTimesheetEntryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class TimesheetServiceImpl implements TimesheetService {

    @Autowired
    private WebClient webClient;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee getTimesheetEntries(ResourceId resourceId, String employeeName, LocalDate dateFrom, LocalDate dateUntil) {
        return employeeMapper.map(webClient.get()
                .uri(uriBuilder -> uriBuilder.path("timesheet")
                        .queryParam("resourceId", resourceId.value())
                        .queryParam("dateFrom", dateFrom.format(ofPattern("yyyy-MM-dd")))
                        .queryParam("dateTo", dateUntil.format(ofPattern("yyyy-MM-dd"))).build())
                .retrieve()
                .bodyToMono(Timesheet.class)
                .block(), resourceId, employeeName);
    }

    @Override
    public boolean createTimesheetEntry(ResourceId resourceId, WorkOrder workOrder, LoggedHoursByDay loggedHoursByDay) {
        CreateTimesheetEntry createTimesheetEntry = new CreateTimesheetEntry(
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

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("timesheet").build())
                .body(Mono.just(createTimesheetEntry), CreateTimesheetEntry.class)
                .retrieve()
                .bodyToMono(CreateTimesheetEntryResult.class)
                .block().isOk()
                ;
    }
}
