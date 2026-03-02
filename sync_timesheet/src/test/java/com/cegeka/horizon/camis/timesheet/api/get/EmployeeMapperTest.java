package com.cegeka.horizon.camis.timesheet.api.get;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.TimesheetLineIdentifier;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    @Test
    void givenExampleInputJson_whenObjectMappedAndEmployeeMapped_createEmployee() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/sampleTimeSheetRetrieval.json");

        ObjectMapper objectMapper = new ObjectMapper();
        Timesheet timesheet = objectMapper.readValue(resourceAsStream, Timesheet.class);
        Employee employee = new EmployeeMapper().map(LocalDate.now(), timesheet, new ResourceId("I123456"), "Ward");

        assertThat(employee.weeklyTimesheets()).hasSize(2);
        assertThat(employee.weeklyTimesheets().get(0).lines()).hasSize(3);
        assertThat(employee.weeklyTimesheets().get(1).lines()).hasSize(2);
        assertThat(employee.weeklyTimesheets().get(1).lines().stream().filter(line -> line.identifier().equals(new TimesheetLineIdentifier("44"))).findFirst().get().loggedHours()).hasSize(4);
    }
}