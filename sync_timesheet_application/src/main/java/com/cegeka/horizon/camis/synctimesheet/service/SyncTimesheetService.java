package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.LoggedHoursByDay;
import com.cegeka.horizon.camis.timesheet.TimesheetLineIdentifier;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.extra.LocalDateRange;

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
                    deleteOriginalLinesWithMatchingWorkOrder(employee);
                    createTimesheetEntry(employee);
                }
            );
    }

    private void deleteOriginalLinesWithMatchingWorkOrder(Employee employeeForEntry) {
        Employee informationCurrentlyInCamis = retrieveOriginalLogging(employeeForEntry);
        employeeForEntry.getUsedWorkOrders();
        informationCurrentlyInCamis.weeklyTimesheets().forEach(weeklyTimesheet -> weeklyTimesheet.lines().stream().filter(line-> employeeForEntry.getUsedWorkOrders().contains(line.workOrder())).forEach(
                line -> {
                    if(!timesheetService.deleteTimesheetEntry(line.identifier(), informationCurrentlyInCamis.resourceId())){
                        logger.error("Failure to delete timesheetLine {} of employee {}", line.identifier().value(), employeeForEntry.name());
                    }
                }
        ));
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
                            logger.info("Update timesheetLine of date {} from employee {}", timesheetLine.startDate(), employee.name());
                        }
                        )
                );
    }

    private Employee retrieveOriginalLogging(Employee employee) {
        LocalDateRange timesheetDuration = employee.getTimesheetDurations();
        Employee camisTimesheetEntries = timesheetService.getTimesheetEntries(employee.resourceId(), employee.name(), timesheetDuration);
        logger.info("Retrieved original Camis timesheet entries for {} with result {}", employee.name(), camisTimesheetEntries);
        return camisTimesheetEntries;
    }

}
