package com.cegeka.horizon.camis.timesheet.api.model;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EmployeeMapper {

    public Employee map(Timesheet timesheet, ResourceId resourceId, String employeeName) {
        Employee employee = new Employee(resourceId, employeeName);
        timesheet.timesheetEntryList.entries.forEach(
                entry -> {
                    WeeklyTimesheet weeklyTimesheet = new WeeklyTimesheet();
                    TimesheetLine lineToAdd = new TimesheetLine(entry.identifier, Status.valueOf(entry.status), entry.description, TimeCode.valueOf(entry.timeCode), new WorkOrder(entry.workOrder));
                    entry.workDayList.workdays.forEach(
                            workDay ->
                            lineToAdd.addLoggedHours(new LoggedHoursByDay(LocalDate.parse(workDay.day, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss")), workDay.hoursWorked))
                    );
                    weeklyTimesheet.addLine(lineToAdd);
                    employee.addWeeklyTimesheet(weeklyTimesheet);
                }
        );
        return employee;
    }
}
