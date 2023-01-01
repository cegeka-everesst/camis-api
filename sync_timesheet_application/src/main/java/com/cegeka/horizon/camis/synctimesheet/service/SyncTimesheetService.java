package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;
import com.cegeka.horizon.camis.timesheet.TimesheetLineIdentifier;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.extra.LocalDateRange;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SyncTimesheetService {

    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    @Autowired
    private TimesheetService timesheetService;

    public void sync(List<Employee> inputEmployees) {
        inputEmployees.forEach(
                employee -> {
                    createTimesheetEntry(employee);
                    retrieveOriginalLogging(employee);
                }
            );
    }

    private void createTimesheetEntry(Employee employee) {
        employee.weeklyTimesheets().forEach(
                weeklyTimesheet -> weeklyTimesheet.lines().forEach(
                        timesheetLine -> {
                            List<LoggedHoursByDay> loggedHours = timesheetLine.loggedHours();
                            Optional<LoggedHoursByDay> firstLoggedHours = loggedHours.stream().findFirst();
                            TimesheetLineIdentifier timesheetLineIdentifier = timesheetService.createTimesheetEntry(employee.resourceId(), timesheetLine.workOrder(), firstLoggedHours.get());
                            loggedHours.stream().skip(1).forEach(
                                    loggedHoursByDay ->
                                            timesheetService.updateTimesheetEntry(timesheetLineIdentifier, employee.resourceId(), timesheetLine.workOrder(), loggedHoursByDay)
                            );
                        }
                        )
                );
    }

    private void retrieveOriginalLogging(Employee employee) {
        LocalDateRange timesheetDuration = employee.getTimesheetDuration();
        Employee camisTimesheetEntries = timesheetService.getTimesheetEntries(employee.resourceId(), employee.name(), timesheetDuration.getStart(), timesheetDuration.getEnd());
        logger.info("Retrieved Camis timesheet entries for {} with result {}", employee.name(),camisTimesheetEntries);
    }

}
