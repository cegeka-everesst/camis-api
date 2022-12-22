package com.cegeka.horizon.camis.workorder.api;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.workorder.WorkOrderAccess;
import com.cegeka.horizon.camis.workorder.WorkorderAccessService;
import com.cegeka.horizon.camis.workorder.api.model.AccessAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class WorkorderAccessServiceImpl implements WorkorderAccessService {

    private final WebClient webClient;

    @Autowired
    public WorkorderAccessServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public WorkOrderAccess isAccessAllowed(ResourceId resourceId, WorkOrder workorder, LocalDate localDate){
        return map(webClient.get()
                .uri(uriBuilder -> uriBuilder.path("wo/access")
                        .queryParam("resourceId", resourceId.value())
                        .queryParam("workorder", workorder.value())
                        .queryParam("date", localDate.format(ofPattern("dd/MM/yyyy"))).build())
                .retrieve()
                .bodyToMono(AccessAllowed.class)
                .block(), resourceId, workorder);
    }

    private WorkOrderAccess map(AccessAllowed access, ResourceId resourceId, WorkOrder workorder) {
        return new WorkOrderAccess(resourceId, workorder, access.isAccessAllowed());
    }
}
