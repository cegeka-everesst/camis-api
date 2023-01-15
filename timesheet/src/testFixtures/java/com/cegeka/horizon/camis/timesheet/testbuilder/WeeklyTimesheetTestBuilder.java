package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.timesheet.TimeCode;
import com.cegeka.horizon.camis.timesheet.TimesheetLine;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.stream;

public class WeeklyTimesheetTestBuilder {
    private final List<TimesheetLine> lines = new ArrayList<>();
    private TimeCode timeCode;

    private WeeklyTimesheetTestBuilder(){}

    public static WeeklyTimesheetTestBuilder aWeeklyTimesheet(){
        return new WeeklyTimesheetTestBuilder();
    }

    public WeeklyTimesheetTestBuilder withLine(TimesheetLine... timesheetLines){
        this.lines.addAll(Arrays.asList(timesheetLines));
        return this;
    }

    public WeeklyTimesheet build(){
        WeeklyTimesheet weeklyTimesheet = new WeeklyTimesheet();
        lines.forEach(weeklyTimesheet::addLine);
        return weeklyTimesheet;
    }
}
