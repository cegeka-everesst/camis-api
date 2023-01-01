package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;

public class TimesheetLine {
    private TimesheetLineIdentifier identifier;
    private Status status;
    private String description;
    private TimeCode timeCode;
    private WorkOrder workOrder;
    private List<LoggedHoursByDay> hoursByDays = new ArrayList<>();


    public TimesheetLine(TimesheetLineIdentifier identifier, Status status, String description, TimeCode timeCode, WorkOrder workOrder) {
        this.identifier = identifier;
        this.status = status;
        this.description = description;
        this.timeCode = timeCode;
        this.workOrder = workOrder;
    }

    public void addLoggedHours(LoggedHoursByDay hoursByDayToAdd){
        Optional<LoggedHoursByDay> sameDay = hoursByDays.stream().filter(loggedHours -> loggedHours.date().equals(hoursByDayToAdd.date())).findFirst();
        if(sameDay.isEmpty()){
            hoursByDays.add(hoursByDayToAdd);
        }else {
            hoursByDays.remove(sameDay.get());
            hoursByDays.add(sameDay.get().add(hoursByDayToAdd));
        }
    }

    public LocalDate startDate() {
        LocalDate localDate = hoursByDays.stream().map(LoggedHoursByDay::date).sorted().findFirst().get();
        if(localDate.getDayOfWeek() == MONDAY){
            return localDate;
        }else{
            return localDate.with(previous(MONDAY));
        }
    }

    public LocalDate endDate() {
        LocalDate localDate = hoursByDays.stream().map(LoggedHoursByDay::date).sorted().findFirst().get();
        if(localDate.getDayOfWeek() == SUNDAY){
            return localDate;
        }else{
            return localDate.with(next(SUNDAY));
        }
    }

    public WorkOrder workOrder() {
        return workOrder;
    }

    public TimesheetLineIdentifier identifier(){
        return identifier;
    }

    public List<LoggedHoursByDay> loggedHours() {
        return hoursByDays.stream().sorted(new LoggedHoursByDay.SortByDate()).toList();
    }

    public WorkOrderStart getFirstUseOfWorkOrder() {
        return new WorkOrderStart(loggedHours().get(0).date(), workOrder);
    }

    public static class SortByStartDate implements java.util.Comparator<TimesheetLine> {
        @Override
        public int compare(TimesheetLine o1, TimesheetLine o2) {
            return o1.startDate().compareTo(o2.startDate());
        }
    }

    @Override
    public String toString() {
        return "TimesheetLine{" + "identifier='" + identifier + '\'' +
                ", workOrder=" + workOrder +
                ", hoursByDays=" + hoursByDays +
                '}';
    }
}
