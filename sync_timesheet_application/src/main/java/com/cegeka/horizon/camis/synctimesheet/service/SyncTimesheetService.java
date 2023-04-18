package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.sync_logger.controller.SyncController;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.synctimesheet.service.command.SyncCommand;
import com.cegeka.horizon.camis.timesheet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.extra.LocalDateRange;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SyncTimesheetService {
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    @Value("${minimumHoursLogged}")
    private double minimumHoursLogged;

    @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private CompareEmployeeService compareEmployeeService;
    @Autowired
    private SyncLoggerService syncLoggerService;
    @Autowired
    private SyncController syncController;

    public void sync(List<Employee> inputEmployees) throws IOException {
        inputEmployees.stream().flatMap(inputEmployee -> inputEmployee.weeklyTimesheets().stream().map(weeklyTimesheet -> new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet))).peek(weeklyTimesheetToSync -> {
            if (!weeklyTimesheetToSync.timesheet().hasMinimumHoursLogged(minimumHoursLogged)) {
                syncLoggerService.logAndAddSyncRecord("Not syncing inputEmployee " + weeklyTimesheetToSync.employee().name() + " timesheet starting at " + weeklyTimesheetToSync.timesheet().startDate() + " due to less than " + minimumHoursLogged + " hours logged", weeklyTimesheetToSync.employee().name(), weeklyTimesheetToSync.timesheet().startDate(), minimumHoursLogged);
            }
        }).filter(weeklyTimesheetToSync -> weeklyTimesheetToSync.timesheet().hasMinimumHoursLogged(minimumHoursLogged)).forEach(weeklyTimesheetToSync -> {
            Optional<WeeklyTimesheet> camisTimesheetForThatPeriod = retrieveOriginalLogging(weeklyTimesheetToSync);
            logger.debug("input information of employee {} : {}", weeklyTimesheetToSync.employee().name(), weeklyTimesheetToSync.employee());
            logger.debug("camis information of employee {} : {}", weeklyTimesheetToSync.employee().name(), camisTimesheetForThatPeriod);

            List<SyncCommand> syncCommands = compareEmployeeService.compare(weeklyTimesheetToSync.employee(), weeklyTimesheetToSync.timesheet(), camisTimesheetForThatPeriod);
            if (syncCommands.stream().anyMatch(SyncCommand::isError)) {
                syncLoggerService.logAndAddSyncRecord("Not syncing inputEmployee " + weeklyTimesheetToSync.employee().name() + " timesheets due to " + syncCommands.stream().filter(SyncCommand::isError).map(SyncCommand::toString).reduce(String::concat).get(), weeklyTimesheetToSync.employee().name());
            } else {
                syncCommands.forEach(syncCommand -> syncCommand.execute(timesheetService, syncLoggerService));
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                logger.error("error while sleeping");
            }
        });
        syncController.sendSyncResult(syncLoggerService.getSyncResultRecordsJson());

        //TODO: retrieve after updates and check correspondences, for example missing holidays
    }

    public void retrieve(List<Employee> inputEmployees) {
        inputEmployees.stream().flatMap(inputEmployee -> inputEmployee.weeklyTimesheets().stream().map(weeklyTimesheet -> new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet))).forEach(this::retrieveOriginalLogging);
    }

    private Optional<WeeklyTimesheet> retrieveOriginalLogging(WeeklyTimesheetToSync weeklyTimesheetToSync) {
        LocalDateRange timesheetDuration = weeklyTimesheetToSync.timesheet().getTimesheetDuration();
        Optional<WeeklyTimesheet> camisTimesheetEntry = timesheetService.getTimesheetEntries(weeklyTimesheetToSync.employee().resourceId(), weeklyTimesheetToSync.employee().name(), timesheetDuration);
        logger.info("Retrieved original Camis timesheet entries for {} with result {} ", weeklyTimesheetToSync.employee().name(), camisTimesheetEntry);
        return camisTimesheetEntry;
    }

}
