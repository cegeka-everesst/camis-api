package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.Status;
import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetLine;

public class TimesheetLineTestBuilder {
    private WorkOrder workorder = new WorkOrder("LMAC001.001");
    private String identifier = "";
    private Status status = Status.T;
    private String description = "description";
    private TimeCode timeCode = TimeCode.AD;

    private TimesheetLineTestBuilder(){}

    public static TimesheetLineTestBuilder aTimesheetLine(){
        return new TimesheetLineTestBuilder();
    }

    public TimesheetLine build(){
        return new TimesheetLine(identifier, status, description, timeCode, workorder);
    }

    public TimesheetLineTestBuilder withWorkOrder(WorkOrder workOrder) {
        this.workorder = workOrder;
        return this;
    }
}
