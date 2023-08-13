package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.domain.WorkOrder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SyncTimesheetService {
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    private final TimesheetService timesheetService;
    private final CompareEmployeeService compareEmployeeService;
    private final SyncLoggerService syncLoggerService;

    public SyncTimesheetService(TimesheetService timesheetService, CompareEmployeeService compareEmployeeService, SyncLoggerService syncLoggerService) {
        this.timesheetService = timesheetService;
        this.compareEmployeeService = compareEmployeeService;
        this.syncLoggerService = syncLoggerService;
    }

    public SyncResult sync(WebClient webClient, List<Employee> inputEmployees, double minimumHoursLogged) {
        SyncResult syncResult = new SyncResult();

        new MinimalDailyHoursLoggedValidator(minimumHoursLogged).validate(inputEmployees, syncResult);

        inputEmployees.stream()
        .flatMap(inputEmployee ->
                        inputEmployee.weeklyTimesheets().stream()
                                .map(weeklyTimesheet -> new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet)))
        .forEach(inputTimesheet -> {
            Optional<WeeklyTimesheet> existingCamisTimesheet = retrieveOriginalLogging(webClient, inputTimesheet);

            logger.debug("input information of employee {} : {}", inputTimesheet.employee().name(), inputTimesheet.employee());
            logger.debug("camis information of employee {} : {}", inputTimesheet.employee().name(), existingCamisTimesheet);

            List<SyncCommand> syncCommands = compareEmployeeService.compare(inputTimesheet.employee(), inputTimesheet.timesheet(), existingCamisTimesheet);
            syncResult.addSyncDays(compareEmployeeService.getSyncDays(inputTimesheet.employee(), inputTimesheet.timesheet(), existingCamisTimesheet));

            if (syncCommands.stream().anyMatch(SyncCommand::isError)) {
                syncResult.addSyncRecord(syncLoggerService.logAndAddSyncRecordWithOtherError(new EmployeeData(inputTimesheet.employee().resourceId(), inputTimesheet.employee().name()), new RecordData(inputTimesheet.timesheet().startDate(),"Not syncing employee " + inputTimesheet.employee().name() + " timesheets due to " + syncCommands.stream().filter(SyncCommand::isError).map(SyncCommand::toString).reduce(String::concat).get(),  null)));
            } else {
                syncCommands.forEach(syncCommand -> syncResult.addSyncRecord(syncCommand.execute(webClient, timesheetService, syncLoggerService)));
            }
            waitBetweenEmployeesToNotOverextentCamisService();
        });
        return syncResult;

        //TODO: retrieve after updates and check correspondences, for example missing holidays
    }



    private static void waitBetweenEmployeesToNotOverextentCamisService() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            logger.error("error while sleeping");
        }
    }

    public void retrieve(WebClient webClient, List<Employee> inputEmployees) {
        inputEmployees.stream()
                .flatMap(
                inputEmployee -> inputEmployee.weeklyTimesheets()
                                        .stream()
                                        .map(weeklyTimesheet -> new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet)))
                .forEach(weeklyTimeSheetToSync -> this.retrieveOriginalLogging(webClient, weeklyTimeSheetToSync));
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

    public void removeDoubleTimesheets(WebClient webClient, List<Employee> inputEmployees) {
        inputEmployees.stream()
                .flatMap(
                        inputEmployee -> inputEmployee.weeklyTimesheets()
                                .stream()
                                .map(weeklyTimesheet -> new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet)))
                .forEach(weeklyTimeSheetToSync -> this.removeDoubleTimesheetsLogging(webClient, weeklyTimeSheetToSync));
    }

    private void removeDoubleTimesheetsLogging(WebClient webClient, WeeklyTimesheetToSync weeklyTimesheetToSync) {
        try{
            TimeUnit.SECONDS.sleep(2);
            LocalDateRange timesheetDuration = weeklyTimesheetToSync.timesheet().getTimesheetDuration();
            Optional<WeeklyTimesheet> camisTimesheetEntry = timesheetService.getTimesheetEntries(webClient, weeklyTimesheetToSync.employee().resourceId(), weeklyTimesheetToSync.employee().name(), timesheetDuration);
            if(camisTimesheetEntry.isPresent()){

                List<TimesheetLineIdentifier> linesToBeDeleted = extractLinesWithExactSameLoggedHoursByDayButDifferentLineIdentifier(camisTimesheetEntry.get().lines().stream().toList());
                if(linesToBeDeleted.size() > 0){
                    logger.error("Deleting double Camis timesheet entries for {} in week {} ", weeklyTimesheetToSync.employee().name(), timesheetDuration);
                }
                    linesToBeDeleted
                            .forEach(lineIdentifier -> timesheetService.deleteTimesheetEntry(webClient, lineIdentifier, weeklyTimesheetToSync.employee().resourceId()));
            }
        }catch (Exception e){
            logger.error("Exception when trying to delete lines", e);
        }
    }

    private List<TimesheetLineIdentifier> extractLinesWithExactSameLoggedHoursByDayButDifferentLineIdentifier(List<TimesheetLine> retrievedLines) {
        return retrievedLines.stream()
                .flatMap(timesheetLine -> timesheetLine.loggedHours().stream().map(loggedHours -> new LoggedHoursTimesheetLineIdentifier(timesheetLine.identifier(), timesheetLine.workOrder(), loggedHours)))
                .collect(Collectors.groupingBy(loggedHoursTimesheetLineIdentifier
                        -> loggedHoursTimesheetLineIdentifier.workOrder.toString() + " "
                        + loggedHoursTimesheetLineIdentifier.loggedHours.date()  + " "
                        + loggedHoursTimesheetLineIdentifier.loggedHours.hours()
                ))
                .values()
                .stream()
                .filter(sameLoggedHoursByDay -> sameLoggedHoursByDay.size() > 1)
                .map(sameLoggedHoursByDay -> sameLoggedHoursByDay.get(0).identifier)
                .toList();
    }

    private static class LoggedHoursTimesheetLineIdentifier{
        private final WorkOrder workOrder;
        private final TimesheetLineIdentifier identifier;
        private final LoggedHoursByDay loggedHours;

        public LoggedHoursTimesheetLineIdentifier(TimesheetLineIdentifier identifier, WorkOrder workOrder, LoggedHoursByDay loggedHours) {
            this.workOrder = workOrder;
            this.identifier = identifier;
            this.loggedHours = loggedHours;
        }
    }
}
