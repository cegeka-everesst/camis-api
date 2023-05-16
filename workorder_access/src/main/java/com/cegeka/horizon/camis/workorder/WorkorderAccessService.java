package com.cegeka.horizon.camis.workorder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

public interface WorkorderAccessService {
    WorkOrderAccess isAccessAllowed(WebClient webClient, ResourceId resourceId, WorkOrder workorder, LocalDate localDate);
}
