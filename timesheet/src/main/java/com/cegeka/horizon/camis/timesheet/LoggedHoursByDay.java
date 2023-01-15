package com.cegeka.horizon.camis.timesheet;

import java.time.LocalDate;

public record LoggedHoursByDay (LocalDate date, double hours) {
    public LoggedHoursByDay add(LoggedHoursByDay hoursByDayToAdd) {
        if(! date.equals(hoursByDayToAdd.date)) throw new IllegalArgumentException("The date must match to add hours");
        return new LoggedHoursByDay(date, hoursByDayToAdd.hours + hours);
    }

    public LoggedHoursByDay minus(double minusHours) {
        return new LoggedHoursByDay(date, this.hours-minusHours);
    }

    public static class SortByDate implements java.util.Comparator<LoggedHoursByDay> {
        @Override
        public int compare(LoggedHoursByDay o1, LoggedHoursByDay o2) {
            return o1.date.compareTo(o2.date);
        }
    }

    @Override
    public String toString() {
        return "LoggedHoursByDay{" + "date=" + date +
                ", hours=" + hours +
                '}';
    }
}
