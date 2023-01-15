package com.cegeka.horizon.camis.timesheet.testbuilder;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.timesheet.Employee;

public class EmployeeTestBuilder {
    private ResourceId resourceId = new ResourceId("I7788899");
    private String name = "description";

    private EmployeeTestBuilder(){}

    public static EmployeeTestBuilder anEmployee(){
        return new EmployeeTestBuilder();
    }

    public Employee build(){
        return new Employee(resourceId, name);
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
