package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;

public record WorkOrderStart(LocalDate start, WorkOrder workOrder) {

    public static class byStart implements java.util.Comparator<WorkOrderStart> {
        @Override
        public int compare(WorkOrderStart o1, WorkOrderStart o2) {
            return o1.start.compareTo(o2.start);
        }
    }
}
