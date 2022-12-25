package com.cegeka.horizon.camis.synctimesheet.csv;

import com.cegeka.horizon.camis.timesheet.Employee;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HoursLoggedCsvReaderTest {

    @Test
    public void giveInputCsv_whenParse_thenCorrectEmployeeContents(){
        InputStream resourceAsStream = getClass().getResourceAsStream("/hoursLoggedCsvReader.csv");
        List<Employee> employees = new HoursLoggedCsvReader(resourceAsStream).readCsv();

        assertThat(employees).hasSize(2);
        assertThat(employees.get(1).weeklyTimesheets().get(0).lines()).hasSize(2);
        assertThat(employees.get(1).weeklyTimesheets().get(0).lines().get(0).loggedHours()).hasSize(2);
        assertThat(employees.get(1).weeklyTimesheets().get(0).lines().get(0).loggedHours().get(0).hours()).isEqualTo(5);
        assertThat(employees.get(1).weeklyTimesheets().get(0).lines().get(0).loggedHours().get(1).hours()).isEqualTo(8);
    }

}