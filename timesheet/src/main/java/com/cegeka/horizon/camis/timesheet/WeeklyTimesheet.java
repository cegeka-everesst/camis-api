package com.cegeka.horizon.camis.timesheet;

import org.threeten.extra.LocalDateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

public class WeeklyTimesheet {
    private final List<TimesheetLine> lines = new ArrayList<>();

    public void addLine(TimesheetLine lineToAdd){
        Optional<TimesheetLine> sameWorkOrder = this.lines.stream().filter(line -> line.workOrder().equals(lineToAdd.workOrder()) && line.identifier().equals(lineToAdd.identifier())).findFirst();
        if(sameWorkOrder.isEmpty()){
            this.lines.add(lineToAdd);
        }else {
            lineToAdd.loggedHours().forEach(
                    loggedHour -> sameWorkOrder.get().addLoggedHours(loggedHour)
            );
        }
    }

    public boolean hasMinimumHoursLogged(double hours) {
        return (hours - getTotalHoursLogged()) < 0.001;
    }

    public LocalDateRange getTimesheetDuration() {
        return LocalDateRange.of(
                this.startDate(),
                this.endDate()
        );
    }

    public LocalDate startDate() {
        if(lines.isEmpty()) throw new IllegalArgumentException("A timesheet needs at least 1 line");
        return lines.stream().map(TimesheetLine::startDate).sorted().findFirst().get();
    }

    public LocalDate endDate() {
        if(lines.isEmpty()) throw new IllegalArgumentException("A timesheet needs at least 1 line");
        return lines.stream().map(TimesheetLine::endDate).max(naturalOrder()).get();
    }

    public List<TimesheetLine> lines() {
        return lines.stream().sorted(new TimesheetLine.SortByStartDateAndWorkOrder()).toList();
    }

    @Override
    public String toString() {
        return "WeeklyTimesheet{" + "lines=" + lines() +
                '}';
    }

    public List<WorkOrderStart> getFirstUseOfWorkOrders() {
        return lines.stream().map(
                TimesheetLine::firstUseOfWorkOrder
        ).collect(toList());
    }

    public double getTotalHoursLogged() {
        return lines.stream().mapToDouble(TimesheetLine::getTotalHoursLogged).sum();
    }

    public boolean hasStartDate(LocalDate startDate) {
        return this.startDate().equals(startDate);
    }

    public Stream<LoggedHoursByDayDetail> loggedHoursDetails() {
        return lines.stream().flatMap(TimesheetLine::loggedHoursDetails);
    }


    public static class SortByStartDate implements java.util.Comparator<WeeklyTimesheet> {
        @Override
        public int compare(WeeklyTimesheet o1, WeeklyTimesheet o2) {
            return o1.startDate().compareTo(o2.startDate());
        }
    }
}
