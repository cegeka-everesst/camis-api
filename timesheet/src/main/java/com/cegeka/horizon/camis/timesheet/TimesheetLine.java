package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.WorkOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;

public class TimesheetLine {
    private final TimesheetLineIdentifier identifier;
    private final Status status;
    private final String description;
    private final TimeCode timeCode;
    private final WorkOrder workOrder;
    private final List<LoggedHoursByDay> hoursByDays = new ArrayList<>();


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

    public TimesheetLineIdentifier identifier(){
        return identifier;
    }

    public List<LoggedHoursByDay> loggedHours() {
        return hoursByDays.stream().sorted(new LoggedHoursByDay.SortByDate()).toList();
    }

    public WorkOrderStart firstUseOfWorkOrder() {
        return new WorkOrderStart(loggedHours().get(0).date(), workOrder);
    }

    public TimeCode timeCode() {
        return timeCode;
    }

    public WorkOrder workOrder() {
        return workOrder;
    }

    public boolean canBeDeleted() {
        return status.canBeDeleted();
    }

    public double getTotalHoursLogged() {
        return hoursByDays.stream().mapToDouble(LoggedHoursByDay::hours).sum();
    }

    public Stream<LoggedHoursByDayDetail> loggedHoursDetails() {
        return loggedHours().stream().flatMap(loggedHoursByDay -> Stream.of(new LoggedHoursByDayDetail(identifier, status, description, timeCode, workOrder, loggedHoursByDay)));
    }

    public static class SortByStartDateAndWorkOrder implements java.util.Comparator<TimesheetLine> {
        @Override
        public int compare(TimesheetLine o1, TimesheetLine o2) {
            if(o1.startDate().equals(o2.startDate())){
                return o1.workOrder.value().compareTo(o2.workOrder.value());
            }
            return o1.startDate().compareTo(o2.startDate());
        }
    }

    @Override
    public String toString() {
        return "TimesheetLine{" + "identifier='" + identifier.value() + '\'' +
                ", workOrder=" + workOrder.value() +
                ", status=" + status.toString() +
                ", timeCode=" + timeCode.value() +
                ", hoursByDays=" + hoursByDays.stream().sorted(new LoggedHoursByDay.SortByDate()).toList() +
                '}';
    }
}
