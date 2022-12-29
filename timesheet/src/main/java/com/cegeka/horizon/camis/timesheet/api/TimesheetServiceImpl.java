package com.cegeka.horizon.camis.timesheet.api;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import com.cegeka.horizon.camis.timesheet.api.model.EmployeeMapper;
import com.cegeka.horizon.camis.timesheet.api.model.Timesheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
}
