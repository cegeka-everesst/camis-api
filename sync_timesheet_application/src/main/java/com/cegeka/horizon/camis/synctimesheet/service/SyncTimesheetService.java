package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.timesheet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.extra.LocalDateRange;

import java.util.List;

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
                    //TODO: retrieveOriginalLogging once again and add check whether it matches with input
                }
        );
    }

    public void retrieve(List<Employee> inputEmployees) {
        inputEmployees.forEach(
                this::retrieveOriginalLogging
        );
    }

    private void deleteOriginalLinesWithMatchingWorkOrder(Employee employeeForEntry) {
        Employee informationCurrentlyInCamis = retrieveOriginalLogging(employeeForEntry);
        employeeForEntry.getUsedWorkOrders();
        informationCurrentlyInCamis.weeklyTimesheets()
                                    .forEach(
                                            weeklyTimesheet -> weeklyTimesheet.lines().stream()
                                                    .filter(TimesheetLine::canBeDeleted)
                                                    .filter(line -> employeeForEntry.getUsedWorkOrders().contains(line.workOrder())).forEach(
                                                    line -> {
                                                            if (!timesheetService.deleteTimesheetEntry(line.identifier(), informationCurrentlyInCamis.resourceId())) {
                                                                logger.error("Failure to delete timesheetLine {} of employee {} ", line.identifier().value(), employeeForEntry.name());
                                                            }
                                                    }
        ));
    }

    private void createTimesheetEntry(Employee employee) {
        employee.weeklyTimesheets().forEach(
                weeklyTimesheet -> weeklyTimesheet.lines().forEach(
                        timesheetLine -> {
                            if (employee.isToSync(timesheetLine.timeCode())) {
                                timesheetLine.loggedHours().stream().forEach(
                                        loggedHoursByDay ->
                                        timesheetService.createTimesheetEntry(employee.resourceId(), timesheetLine.timeCode(), timesheetLine.workOrder(), loggedHoursByDay)
                                );
                                logger.info("Updated timesheetLine of date {} with workOrder {} from employee {} ", timesheetLine.startDate(), timesheetLine.workOrder(), employee.name());
                            }
                        }
                )
        );
    }

    private Employee retrieveOriginalLogging(Employee employee) {
        LocalDateRange timesheetDuration = employee.getTimesheetDurations();
        Employee camisTimesheetEntries = timesheetService.getTimesheetEntries(employee.resourceId(), employee.name(), timesheetDuration);
        logger.info("Retrieved original Camis timesheet entries for {} with result {} ", employee.name(), camisTimesheetEntries);
        return camisTimesheetEntries;
    }

}
