package com.cegeka.horizon.camis.synctimesheet.csv;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public record HoursLoggedByDay(LocalDate localDate,
                               ResourceId resourceId,
                               String employeeName,
                               WorkOrder workOrder,
                               double hoursLogged) {
}
