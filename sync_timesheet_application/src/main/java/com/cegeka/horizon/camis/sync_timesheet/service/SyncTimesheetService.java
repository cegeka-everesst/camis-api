package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.sync_logger.model.SyncDay;
import com.cegeka.horizon.camis.sync_logger.model.SyncResult;
import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.sync_timesheet.service.command.SyncCommand;
import com.cegeka.horizon.camis.timesheet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.threeten.extra.LocalDateRange;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SyncTimesheetService {
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    //@Value("${minimumHoursLogged}")
    private final double minimumHoursLogged;
    private final TimesheetService timesheetService;
    private final CompareEmployeeService compareEmployeeService;
    private final SyncLoggerService syncLoggerService;

    public SyncTimesheetService(TimesheetService timesheetService, CompareEmployeeService compareEmployeeService, SyncLoggerService syncLoggerService) {
        this.minimumHoursLogged = 40;
        this.timesheetService = timesheetService;
        this.compareEmployeeService = compareEmployeeService;
        this.syncLoggerService = syncLoggerService;
    }

    public SyncResult sync(WebClient webClient, List<Employee> inputEmployees) {
        SyncResult syncResult = new SyncResult();
        inputEmployees.stream().flatMap(inputEmployee -> inputEmployee.weeklyTimesheets().stream().map(weeklyTimesheet ->
                new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet))).peek(weeklyTimesheetToSync -> {
            if (!weeklyTimesheetToSync.timesheet().hasMinimumHoursLogged(minimumHoursLogged)) {
                syncResult.addSyncDays(getSyncDaysMinimumHours(weeklyTimesheetToSync));
                syncResult.addSyncRecord(syncLoggerService.logAndAddSyncRecordWithHoursMinimum(new EmployeeData(weeklyTimesheetToSync.employee().resourceId(), weeklyTimesheetToSync.employee().name(), weeklyTimesheetToSync.employee().getSlackName()), new RecordData(weeklyTimesheetToSync.timesheet().startDate(), "Not syncing inputEmployee " + weeklyTimesheetToSync.employee().name() + " timesheet starting at " + weeklyTimesheetToSync.timesheet().startDate() + " due to less than " + minimumHoursLogged + " hours logged",null), minimumHoursLogged));
            }
        }).filter(weeklyTimesheetToSync -> weeklyTimesheetToSync.timesheet().hasMinimumHoursLogged(minimumHoursLogged)).forEach(weeklyTimesheetToSync -> {
            Optional<WeeklyTimesheet> camisTimesheetForThatPeriod = retrieveOriginalLogging(webClient, weeklyTimesheetToSync);

            logger.debug("input information of employee {} : {}", weeklyTimesheetToSync.employee().name(), weeklyTimesheetToSync.employee());
            logger.debug("camis information of employee {} : {}", weeklyTimesheetToSync.employee().name(), camisTimesheetForThatPeriod);

            List<SyncCommand> syncCommands = compareEmployeeService.compare(weeklyTimesheetToSync.employee(), weeklyTimesheetToSync.timesheet(), camisTimesheetForThatPeriod);
            syncResult.addSyncDays(compareEmployeeService.getSyncDays(weeklyTimesheetToSync.employee(), weeklyTimesheetToSync.timesheet(), camisTimesheetForThatPeriod));

            if (syncCommands.stream().anyMatch(SyncCommand::isError)) {
                syncResult.addSyncRecord(syncLoggerService.logAndAddSyncRecordWithOtherError(new EmployeeData(weeklyTimesheetToSync.employee().resourceId(), weeklyTimesheetToSync.employee().name(), weeklyTimesheetToSync.employee().getSlackName()), new RecordData(weeklyTimesheetToSync.timesheet().startDate(),"Not syncing employee " + weeklyTimesheetToSync.employee().name() + " timesheets due to " + syncCommands.stream().filter(SyncCommand::isError).map(SyncCommand::toString).reduce(String::concat).get(),  null)));
            } else {
                syncCommands.forEach(syncCommand -> syncResult.addSyncRecord(syncCommand.execute(webClient, timesheetService, syncLoggerService)));
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                logger.error("error while sleeping");
            }
        });
        return syncResult;

        //TODO: retrieve after updates and check correspondences, for example missing holidays
    }

    public void retrieve(WebClient webClient, List<Employee> inputEmployees) {
        inputEmployees.stream().flatMap(inputEmployee -> inputEmployee.weeklyTimesheets().stream().map(weeklyTimesheet -> new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet))).forEach(weeklyTimeSheetToSync -> this.retrieveOriginalLogging(webClient, weeklyTimeSheetToSync));
    }

    private Optional<WeeklyTimesheet> retrieveOriginalLogging(WebClient webClient, WeeklyTimesheetToSync weeklyTimesheetToSync) {
        LocalDateRange timesheetDuration = weeklyTimesheetToSync.timesheet().getTimesheetDuration();
        Optional<WeeklyTimesheet> camisTimesheetEntry = timesheetService.getTimesheetEntries(webClient, weeklyTimesheetToSync.employee().resourceId(), weeklyTimesheetToSync.employee().name(), timesheetDuration);
        logger.info("Retrieved original Camis timesheet entries for {} with result {} ", weeklyTimesheetToSync.employee().name(), camisTimesheetEntry);
        return camisTimesheetEntry;
    }

    private List<SyncDay> getSyncDaysMinimumHours(WeeklyTimesheetToSync weeklyTimesheetToSync) {
        List<SyncDay> syncDays = new ArrayList<>();
        weeklyTimesheetToSync.timesheet().loggedHoursDetails().forEach(loggedHoursByDayDetail ->
                syncDays.add(new SyncDay(weeklyTimesheetToSync.employee().resourceId(),
                        loggedHoursByDayDetail.loggedHoursByDay().date(),
                        loggedHoursByDayDetail.workOrder(),-1,
                        loggedHoursByDayDetail.loggedHoursByDay().hours())));
        return syncDays;
    }
}
