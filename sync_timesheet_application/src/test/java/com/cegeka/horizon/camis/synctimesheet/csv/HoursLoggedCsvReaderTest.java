package com.cegeka.horizon.camis.synctimesheet.csv;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.TimesheetLine;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

class HoursLoggedCsvReaderTest {

    @Test
    public void giveInputCsv_whenParse_thenCorrectEmployeeContents(){
        InputStream resourceAsStream = getClass().getResourceAsStream("/hoursLoggedCsvReader.csv");
        List<Employee> employees = new HoursLoggedCsvReader(resourceAsStream).readCsv();

        assertThat(employees).hasSize(2);
        Employee employeeBen = employees.get(1);
        assertThat(employeeBen.weeklyTimesheets().get(0).lines()).hasSize(2);
        TimesheetLine timesheetLineLMAC005 = employeeBen.weeklyTimesheets().get(0).lines().get(1);
        assertThat(timesheetLineLMAC005.workOrder()).isEqualTo(new WorkOrder("LMAC000.005"));
        assertThat(timesheetLineLMAC005.loggedHours()).hasSize(2);
        assertThat(timesheetLineLMAC005.loggedHours().get(0).hours()).isEqualTo(5);
        assertThat(timesheetLineLMAC005.loggedHours().get(0).date()).isEqualTo(of(2022,12,23));
        assertThat(timesheetLineLMAC005.loggedHours().get(1).hours()).isEqualTo(8);
        assertThat(timesheetLineLMAC005.loggedHours().get(1).date()).isEqualTo(of(2022,12,24));
    }

}