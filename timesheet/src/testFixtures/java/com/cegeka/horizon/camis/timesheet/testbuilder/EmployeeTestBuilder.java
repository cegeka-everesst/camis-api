package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;

public class EmployeeTestBuilder {
    private ResourceId resourceId = new ResourceId("I778899");
    private String name = "description";
    private final List<WeeklyTimesheetTestBuilder> weeklyTimesheets = new ArrayList<>();

    private EmployeeTestBuilder(){}

    public static EmployeeTestBuilder anEmployee(){
        return new EmployeeTestBuilder();
    }

    public EmployeeTestBuilder withTimeSheet(WeeklyTimesheetTestBuilder... weeklyTimesheets){
        this.weeklyTimesheets.addAll(of(weeklyTimesheets));
        return this;
    }

    public Employee build(){
        Employee employee = new Employee(resourceId, name);
        weeklyTimesheets.stream().map(WeeklyTimesheetTestBuilder::build).forEach(
                employee::addWeeklyTimesheet
        );
        return employee;
    }

    public EmployeeTestBuilder withIdentifier(String identifier) {
        this.resourceId = new ResourceId(identifier);
        return this;
    }

    public EmployeeTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

}
