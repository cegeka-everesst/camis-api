package com.cegeka.horizon.camis.timesheet;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.cegeka.horizon.camis.timesheet.testbuilder.LoggedHoursByDayTestBuilder.aLoggedHours;
import static com.cegeka.horizon.camis.timesheet.testbuilder.TimesheetLineTestBuilder.aTimesheetLine;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

class TimesheetLineTest {

    @Test
    public void givenHoursByDay_whenStartDate_mondayOfLoggedHours(){
        TimesheetLine line = aTimesheetLine().build();
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,25)).build());
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,22)).build());
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,21)).build());

        assertThat(line.startDate()).isEqualTo(of(2022,12, 19));
    }

    @Test
    public void givenHoursByDay_whenStartDate_mondayOfLoggedHoursWhereOneIsAMonday(){
        TimesheetLine line = aTimesheetLine().build();
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,22)).build());
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,19)).build());

        assertThat(line.startDate()).isEqualTo(of(2022,12, 19));
    }

    @Test
    public void givenHoursByDay_whenEndDate_sundayOfLoggedHours(){
        TimesheetLine line = aTimesheetLine().build();
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,22)).build());
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,21)).build());

        assertThat(line.endDate()).isEqualTo(of(2022,12, 25));
    }

    @Test
    public void givenHoursByDay_whenEndDate_sundayOfLoggedHoursWhereOneIsASunday(){
        TimesheetLine line = aTimesheetLine().build();
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,22)).build());
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,25)).build());
        line.addLoggedHours(aLoggedHours().withDay(of(2022,12,19)).build());

        assertThat(line.endDate()).isEqualTo(of(2022,12, 25));
    }

}