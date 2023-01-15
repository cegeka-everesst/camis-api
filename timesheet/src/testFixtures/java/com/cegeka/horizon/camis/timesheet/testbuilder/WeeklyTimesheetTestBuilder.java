package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetLine;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.List.of;

public class WeeklyTimesheetTestBuilder {
    private final List<TimesheetLineTestBuilder> lines = new ArrayList<>();
    private TimeCode timeCode;

    private WeeklyTimesheetTestBuilder(){}

    public static WeeklyTimesheetTestBuilder aWeeklyTimesheet(){
        return new WeeklyTimesheetTestBuilder();
    }

    public WeeklyTimesheetTestBuilder withLine(TimesheetLineTestBuilder... timesheetLines){
        this.lines.addAll(of(timesheetLines));
        return this;
    }

    public WeeklyTimesheet build(){
        WeeklyTimesheet weeklyTimesheet = new WeeklyTimesheet();
        lines.stream().map(TimesheetLineTestBuilder::build).forEach(weeklyTimesheet::addLine);
        return weeklyTimesheet;
    }
}
