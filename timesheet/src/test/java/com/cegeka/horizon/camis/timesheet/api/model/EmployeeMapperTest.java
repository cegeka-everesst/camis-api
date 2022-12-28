package com.cegeka.horizon.camis.timesheet.api.model;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    @Test
    public void givenExampleInputJson_whenObjectMappedAndEmployeeMapped_createEmployee() throws IOException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/sampleTimeSheetRetrieval.json");

        ObjectMapper objectMapper = new ObjectMapper();
        Timesheet timesheet = objectMapper.readValue(resourceAsStream, Timesheet.class);
        Employee employee = new EmployeeMapper().map(timesheet, new ResourceId("I123"), "Ward");

        assertThat(employee.weeklyTimesheets()).hasSize(2);
        assertThat(employee.weeklyTimesheets().get(0).lines()).hasSize(3);
        assertThat(employee.weeklyTimesheets().get(1).lines()).hasSize(2);
        assertThat(employee.weeklyTimesheets().get(1).lines().stream().filter(line -> line.identifier().equals("44")).findFirst().get().loggedHours()).hasSize(4);

    }

}