package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.synctimesheet.service.command.CreateTimesheetEntryCommand;
import com.cegeka.horizon.camis.synctimesheet.service.command.ErrorCommand;
import com.cegeka.horizon.camis.synctimesheet.service.command.NothingToSyncCommand;
import com.cegeka.horizon.camis.synctimesheet.service.command.SyncCommand;
import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.LoggedHoursByDayDetail;
import com.cegeka.horizon.camis.timesheet.WeeklyTimesheet;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

@Service
public class CompareEmployeeService {

    public List<SyncCommand> compare(Employee inputEmployee, WeeklyTimesheet inputTimeSheet, Optional<WeeklyTimesheet> camisTimesheetForThatPeriod) {
        List<SyncCommand> result = new ArrayList<>();

        // loggedHoursByDayDetailsInput is sum-collapsed by date & workorder by definition (from csv read)
        Stream<LoggedHoursByDayDetail> loggedHoursByDayDetailsInput = inputTimeSheet.loggedHoursDetails();
        // loggedHoursByDayDetailsCamis is NOT sum-collapsed by date & workorder if identifier differs like with multiple lines (from camis api get)
        List<LoggedHoursByDayDetail> loggedHoursByDayDetailsCamis = getLoggedHoursByDayDetails(camisTimesheetForThatPeriod);

        loggedHoursByDayDetailsInput
                .filter(inputHours -> inputEmployee.isToSync(inputHours.timeCode()))
                .forEach(
            inputHours -> {
                List<LoggedHoursByDayDetail> camisHours =  findByWorkOrderAndDate(loggedHoursByDayDetailsCamis, inputHours.workOrder(), inputHours.loggedHoursByDay().date());
                if(camisHours.isEmpty()){
                    result.add(new CreateTimesheetEntryCommand(inputEmployee.name(), inputEmployee.resourceId(), inputHours.workOrder(), inputHours.loggedHoursByDay(), inputHours.timeCode()));
                }else{
                    if(abs(sum(camisHours) - inputHours.loggedHoursByDay().hours()) < 0.01){
                        result.add(new NothingToSyncCommand(inputEmployee.name(), inputHours.workOrder(), inputHours.loggedHoursByDay().date()));
                    }else if(sum(camisHours) < inputHours.loggedHoursByDay().hours()){
                        result.add(new CreateTimesheetEntryCommand(inputEmployee.name(), inputEmployee.resourceId(), inputHours.workOrder(), inputHours.loggedHoursByDay().minus(sum(camisHours)), inputHours.timeCode()));
                    }else{
                        result.add(new ErrorCommand(inputEmployee.name() + " sum(camisHours) > sum(tempoHours) on " + inputHours.loggedHoursByDay().date()));
                        //TODO: deleting lines and adding, for now we just throw an error,
                    }
                }
            }
        );

        return result;
    }

    private List<LoggedHoursByDayDetail> getLoggedHoursByDayDetails(Optional<WeeklyTimesheet> camisTimesheetForThatPeriod) {
        return camisTimesheetForThatPeriod.map(weeklyTimesheet -> weeklyTimesheet.loggedHoursDetails().collect(toList())).orElseGet(ArrayList::new);
    }

    private double sum(List<LoggedHoursByDayDetail> camisHours) {
        return camisHours.stream().mapToDouble(camisHour -> camisHour.loggedHoursByDay().hours()).sum();
    }

    private List<LoggedHoursByDayDetail> findByWorkOrderAndDate(List<LoggedHoursByDayDetail> camisHoursDetails, WorkOrder workOrder, LocalDate date) {
        return camisHoursDetails.stream().filter(camisHours -> camisHours.workOrder().equals(workOrder)).filter(camisHours -> camisHours.loggedHoursByDay().date().equals(date)).collect(toList());
    }


}
