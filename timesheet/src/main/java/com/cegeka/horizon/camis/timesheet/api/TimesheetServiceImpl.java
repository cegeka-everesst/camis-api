package com.cegeka.horizon.camis.timesheet.api;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import com.cegeka.horizon.camis.timesheet.api.model.Timesheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class TimesheetServiceImpl implements TimesheetService {

    private final WebClient webClient;

    @Autowired
    public TimesheetServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getTimesheetEntries(ResourceId resourceId, LocalDate dateFrom, LocalDate dateTo) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("timesheet")
                        .queryParam("resourceId", resourceId.value())
                        .queryParam("dateFrom", dateFrom.format(ofPattern("dd/MM/yyyy")))
                        .queryParam("dateTo", dateTo.format(ofPattern("dd/MM/yyyy"))).build())
                .retrieve()
                .bodyToMono(Timesheet.class)
                .block().toString();
    }
}
