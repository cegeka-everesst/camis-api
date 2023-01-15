package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.Status;
import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetLine;
import com.cegeka.horizon.camis.timesheet.TimesheetLineIdentifier;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;

public class TimesheetLineTestBuilder {
    private WorkOrder workorder = new WorkOrder("LMAC001.001");
    private TimesheetLineIdentifier identifier = TimesheetLineIdentifier.TO_CREATE;
    private Status status = Status.TRANSFERRED;
    private String description = "description";
    private TimeCode timeCode = TimeCode.WORK_DAY;
    private List<LoggedHoursByDayTestBuilder> loggedHours = new ArrayList<>();

    private TimesheetLineTestBuilder(){}

    public static TimesheetLineTestBuilder aTimesheetLine(){
        return new TimesheetLineTestBuilder();
    }

    public TimesheetLine build(){
        TimesheetLine timesheetLine = new TimesheetLine(identifier, status, description, timeCode, workorder);
        loggedHours.forEach(
                loggedHour ->
                timesheetLine.addLoggedHours(loggedHour.build())
        );
        return timesheetLine;
    }

    public TimesheetLineTestBuilder withWorkOrder(WorkOrder workOrder) {
        this.workorder = workOrder;
        return this;
    }

    public TimesheetLineTestBuilder withIdentifier(TimesheetLineIdentifier identifier) {
        this.identifier = identifier;
        return this;
    }

    public TimesheetLineTestBuilder withLoggedHours(LoggedHoursByDayTestBuilder... aLoggedHours) {
        this.loggedHours.addAll(of(aLoggedHours));
        return this;
    }
}
