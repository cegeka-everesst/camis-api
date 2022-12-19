package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.api.workorder.WorkOrderAccess;
import com.cegeka.horizon.camis.synctimesheet.domain.ResourceId;
import com.cegeka.horizon.camis.synctimesheet.domain.Workorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class WorkorderAccessService {

    private final WebClient webClient;

    @Autowired
    public WorkorderAccessService(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean isAccessAllowed(ResourceId resourceId, Workorder workorder, LocalDate localDate){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("wo/access")
                        .queryParam("resourceId", resourceId.value())
                        .queryParam("workorder", workorder.value())
                        .queryParam("date", localDate.format(ofPattern("dd/MM/YYYY"))).build())
                .retrieve()
                .bodyToMono(WorkOrderAccess.class)
                .block().isAccessAllowed();
    }
}
