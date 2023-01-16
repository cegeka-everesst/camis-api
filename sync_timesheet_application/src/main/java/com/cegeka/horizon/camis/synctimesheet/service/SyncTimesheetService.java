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
                .stream()
                .peek(employee -> {
                    if(! employee.hasMinimum40HoursLogged()){
                        logger.error("Not syncing employee {} timesheet starting at due to less than 40 hours logged some weeks", employee.name());
                    }
                })
                .filter(Employee::hasMinimum40HoursLogged)
                .forEach(
                    inputEmployee -> {
                        Employee camisEmployee = retrieveOriginalLogging(inputEmployee);
                        logger.debug("input information of employee {} : {}", inputEmployee.name(), inputEmployee);
                        logger.debug("camis information of employee {} : {}", inputEmployee.name(), camisEmployee);
                        List<SyncCommand> syncCommands = compareEmployeeService.compare(inputEmployee, camisEmployee);
                        if(syncCommands.stream().anyMatch(SyncCommand::isError)){
                            logger.error("Not syncing employee {} timesheets due to {}", inputEmployee.name(), syncCommands.stream().filter(SyncCommand::isError).map(SyncCommand::toString).reduce(String::concat).get());
                        }else{
                            syncCommands.forEach(syncCommand -> syncCommand.execute(timesheetService));
                        }
                    }
        );

        //TODO: retrieve after updates and check correspondences, for example missing holidays
    }

    public void retrieve(List<Employee> inputEmployees) {
        inputEmployees.forEach(
                this::retrieveOriginalLogging
        );
    }

    private Employee retrieveOriginalLogging(Employee employee) {
        LocalDateRange timesheetDuration = employee.getTimesheetDurations();
        Employee camisTimesheetEntries = timesheetService.getTimesheetEntries(employee.resourceId(), employee.name(), timesheetDuration);
        logger.info("Retrieved original Camis timesheet entries for {} with result {} ", employee.name(), camisTimesheetEntries);
        return camisTimesheetEntries;
    }

}
