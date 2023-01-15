package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.synctimesheet.service.command.SyncCommand;
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
    @Autowired
    private CompareEmployeeService compareEmployeeService;

    public void sync(List<Employee> inputEmployees) {
        inputEmployees
                .forEach(
                inputEmployee -> {
                        Employee camisEmployee = retrieveOriginalLogging(inputEmployee);
                        compareEmployeeService.compare(inputEmployee, camisEmployee).forEach(syncCommand -> syncCommand.execute(timesheetService));
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
        employee.weeklyTimesheets()
                .stream()
                .peek(weeklyTimesheet -> {
                    if(weeklyTimesheet.getTotalHoursLogged() < 40.0 ){
                        logger.error("Not syncing employee {} timesheet starting at {} due to less than 40 hours logged", employee.name(), weeklyTimesheet.startDate());
                    }
                })
                .filter(weeklyTimesheet -> weeklyTimesheet.getTotalHoursLogged() >= 40.0)
                .forEach(
                weeklyTimesheet -> weeklyTimesheet.lines().forEach(
                        timesheetLine -> {
                            if (employee.isToSync(timesheetLine.timeCode())) {
                                timesheetLine.loggedHours().forEach(
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
