package com.cegeka.horizon.camis.sync_timesheet.service;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
import com.cegeka.horizon.camis.sync.logger.model.result.CamisWorkorderInfo;
import com.cegeka.horizon.camis.sync.logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.sync_timesheet.service.command.SyncCommand;
import com.cegeka.horizon.camis.timesheet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.threeten.extra.LocalDateRange;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Flux.fromStream;

@Service
public class SyncTimesheetService {
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    private final TimesheetService timesheetService;
    private final CompareEmployeeService compareEmployeeService;

    public SyncTimesheetService(TimesheetService timesheetService, CompareEmployeeService compareEmployeeService) {
        this.timesheetService = timesheetService;
        this.compareEmployeeService = compareEmployeeService;
    }

    public Flux<SyncResult> sync(WebClient webClient, List<Employee> inputEmployees, double minimumHoursLogged) {
        Flux<SyncResult> minimalHoursValidation =
                fromStream(new MinimalDailyHoursLoggedValidator(minimumHoursLogged)
                        .validate(inputEmployees));

        Flux<SyncResult> syncResults =
                fromIterable(inputEmployees)
                .flatMap(inputEmployee ->
                        fromStream(inputEmployee.weeklyTimesheets().stream()
                                .map(weeklyTimesheet -> new WeeklyTimesheetToSync(inputEmployee, weeklyTimesheet))))
                .flatMap(
                    inputTimesheet -> {

                        Optional<WeeklyTimesheet> existingCamisTimesheet = retrieveOriginalLogging(webClient, inputTimesheet);

                        logger.debug("input information of employee {} : {}", inputTimesheet.employee().name(), inputTimesheet.employee());
                        logger.debug("camis information of employee {} : {}", inputTimesheet.employee().name(), existingCamisTimesheet);

                        List<SyncCommand> syncCommands = compareEmployeeService.compare(inputTimesheet.employee(), inputTimesheet.timesheet(), existingCamisTimesheet);

                        if (syncCommands.stream().anyMatch(SyncCommand::isError)) {
                            return Flux.just(combineErrorMessages(inputTimesheet, syncCommands));
                        } else {
                            return Flux.fromIterable(syncCommands).flatMap(syncCommand -> Flux.just(syncCommand.execute(webClient, timesheetService)));
                        }
                    }
                );
        return Flux.concat(minimalHoursValidation, syncResults)
                .doOnNext(syncResult -> new SyncLoggerService().log(syncResult))
                .onBackpressureBuffer();

        //TODO: retrieve after updates and check correspondences, for example missing holidays
    }

    private SyncResult combineErrorMessages(WeeklyTimesheetToSync inputTimesheet, List<SyncCommand> syncCommands) {
        return
                SyncResult.otherSyncError(
                        new EmployeeIdentification(inputTimesheet.employee().resourceId(),
                                inputTimesheet.employee().name()),
                        new CamisWorkorderInfo(inputTimesheet.timesheet().startDate(),
                                String.format("Not syncing any of employee %s timesheets due to %s",
                                        inputTimesheet.employee().name(),
                                        syncCommands.stream().filter(SyncCommand::isError).map(SyncCommand::toString).collect(joining(" , \n"))),
                                WorkOrder.empty()));
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
        waitBetweenEmployeesToNotOverextentCamisService();
        return camisTimesheetEntry;
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
            waitBetweenEmployeesToNotOverextentCamisService();
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

    private static void waitBetweenEmployeesToNotOverextentCamisService() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.error("error while sleeping");
        }
    }
}
