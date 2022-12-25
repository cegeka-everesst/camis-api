package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;

import java.time.LocalDate;

public class LoggedHoursByDayTestBuilder {
    private double hours = 8;
    private LocalDate localDate = LocalDate.of(2022,12,25);

    private  LoggedHoursByDayTestBuilder(){}
    public static LoggedHoursByDayTestBuilder aLoggedHours(){
        return new LoggedHoursByDayTestBuilder();
    }
    public static LoggedHoursByDayTestBuilder aLoggedHours(double hours){
        LoggedHoursByDayTestBuilder loggedHoursByDayTestBuilder = new LoggedHoursByDayTestBuilder();
        loggedHoursByDayTestBuilder.hours = hours;
        return loggedHoursByDayTestBuilder;
    }

    public LoggedHoursByDayTestBuilder withDay(int day) {
        this.localDate = this.localDate.withDayOfMonth(day);
        return this;
    }

    public LoggedHoursByDayTestBuilder withDay(LocalDate date) {
        this.localDate = date;
        return this;
    }

    public LoggedHoursByDay build(){
        return new LoggedHoursByDay(localDate, hours);
    }

}
