package com.cegeka.horizon.camis.workorder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.Workorder;

import java.time.LocalDate;

public interface WorkorderAccessService {
    WorkOrderAccess isAccessAllowed(ResourceId resourceId, Workorder workorder, LocalDate localDate);
}
