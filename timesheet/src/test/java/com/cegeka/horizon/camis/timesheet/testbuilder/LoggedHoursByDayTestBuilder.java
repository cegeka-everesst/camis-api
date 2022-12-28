package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;

import java.time.LocalDate;

public class LoggedHoursByDayTestBuilder {
    private double hours = 8;
    private LocalDate date = LocalDate.of(2022,12,25);

    private  LoggedHoursByDayTestBuilder(){}
    public static LoggedHoursByDayTestBuilder aLoggedHours(){
        return new LoggedHoursByDayTestBuilder();
    }
    public static LoggedHoursByDayTestBuilder aLoggedHours(double hours){
        LoggedHoursByDayTestBuilder loggedHoursByDayTestBuilder = new LoggedHoursByDayTestBuilder();
        loggedHoursByDayTestBuilder.hours = hours;
        return loggedHoursByDayTestBuilder;
    }

    public static LoggedHoursByDayTestBuilder aLoggedHours(double hours, LocalDate date){
        LoggedHoursByDayTestBuilder loggedHoursByDayTestBuilder = new LoggedHoursByDayTestBuilder();
        loggedHoursByDayTestBuilder.hours = hours;
        loggedHoursByDayTestBuilder.date = date;
        return loggedHoursByDayTestBuilder;
    }

    public LoggedHoursByDayTestBuilder withDay(int day) {
        this.date = this.date.withDayOfMonth(day);
        return this;
    }

    public LoggedHoursByDayTestBuilder withDay(LocalDate date) {
        this.date = date;
        return this;
    }

    public LoggedHoursByDay build(){
        return new LoggedHoursByDay(date, hours);
    }

}
