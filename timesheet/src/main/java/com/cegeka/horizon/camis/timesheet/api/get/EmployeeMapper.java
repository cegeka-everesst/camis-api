package com.cegeka.horizon.camis.timesheet.api.get;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class EmployeeMapper {

    public Employee map(LocalDate start, Timesheet timesheet, ResourceId resourceId, String employeeName) {
        Employee employee = new Employee(resourceId, employeeName);
        timesheet.timesheetEntryList.entries.forEach(
                entry -> {
                    if(entry.identifier != null){
                        WeeklyTimesheet weeklyTimesheet = new WeeklyTimesheet();
                        TimesheetLine lineToAdd = new TimesheetLine(new TimesheetLineIdentifier(entry.identifier), Status.map(entry.status), entry.description, new TimeCode(entry.timeCode), new WorkOrder(entry.workOrder));
                        if(! lineToAdd.workOrder().isEmpty()){
                            entry.workDayList.workdays.forEach(
                                    workDay ->
                                            lineToAdd.addLoggedHours(new LoggedHoursByDay(LocalDate.parse(workDay.day, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss")), workDay.hoursWorked))
                            );
                            weeklyTimesheet.addLine(lineToAdd);
                            employee.addWeeklyTimesheet(weeklyTimesheet);
                        }
                        else{
                            System.out.println("ERROR : Empty workorder in Camis for employee " + employeeName + " line with date " + start);
                        }
                    }
                }
        );
        return employee;
    }
}
