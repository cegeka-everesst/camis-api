package com.cegeka.horizon.camis.synctimesheet.csv;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.TimeCode;

import java.time.LocalDate;

public record HoursLoggedByDay(LocalDate localDate,
                               ResourceId resourceId,
                               String employeeName,
                               TimeCode timeCode,
                               WorkOrder workOrder,
                               double hoursLogged) {
}
