package com.cegeka.horizon.camis.timesheet;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class LoggedHoursByDayTest {

    @Test
    public void givenHoursByDay_whenHours_roundCorrectly(){
        LoggedHoursByDay loggedHoursByDay = new LoggedHoursByDay(LocalDate.now(), 2.4000000000000004);
        assertThat(loggedHoursByDay.hours()).isEqualTo(2.4);
        assertThat(loggedHoursByDay.hours()).isEqualTo(2.40);
    }


}