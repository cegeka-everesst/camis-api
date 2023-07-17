package com.cegeka.horizon.camis.workorder.api;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.workorder.WorkOrderAccess;
import com.cegeka.horizon.camis.workorder.WorkorderAccessService;
import com.cegeka.horizon.camis.workorder.api.model.AccessAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class WorkorderAccessServiceImpl implements WorkorderAccessService {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderAccessServiceImpl.class);

    @Override
    public WorkOrderAccess isAccessAllowed(WebClient webClient, ResourceId resourceId, WorkOrder workorder, LocalDate date){
        logger.info("Webrequest to check work order access for {} {} {}", resourceId.value(), workorder.value(), date.format(ofPattern("MM/dd/yyyy")));
        return map(Objects.requireNonNull(webClient.get()
                .uri(uriBuilder -> uriBuilder.path("wo/access")
                        .queryParam("resourceId", resourceId.value())
                        .queryParam("workorder", workorder.value())
                        .queryParam("date", date).build())
                .retrieve()
                .bodyToMono(AccessAllowed.class)
                .block()), resourceId, workorder);
    }

    private WorkOrderAccess map(AccessAllowed access, ResourceId resourceId, WorkOrder workorder) {
        return new WorkOrderAccess(resourceId, workorder, access.isAccessAllowed());
    }
}
