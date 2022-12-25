package com.cegeka.horizon.camis.synctimesheet.csv;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.timesheet.*;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;

public class HoursLoggedCsvReader {

    private final InputStream csvInputStream;

    public HoursLoggedCsvReader(InputStream inputStream){
        this.csvInputStream = inputStream;
    }


    public List<Employee> readCsv() {
        List<HoursLoggedByDay> hoursLoggedByDays = parseCsv();
        return mapToEmployeeTimesheets(hoursLoggedByDays);
    }

    private List<Employee> mapToEmployeeTimesheets(List<HoursLoggedByDay> hoursLoggedByDays) {
        Map<ResourceId, Employee> employees = new HashMap<>();
        hoursLoggedByDays.forEach(
            hoursLoggedByDay -> {
                Employee employee = new Employee(hoursLoggedByDay.resourceId(), hoursLoggedByDay.employeeName());
                WeeklyTimesheet weeklyTimesheet = new WeeklyTimesheet();
                TimesheetLine timesheetLine = new TimesheetLine("", Status.T, "", TimeCode.AD, hoursLoggedByDay.workOrder());
                timesheetLine.addLoggedHours(new LoggedHoursByDay(hoursLoggedByDay.localDate(), hoursLoggedByDay.hoursLogged()));
                weeklyTimesheet.addLine(timesheetLine);
                employee.addWeeklyTimesheet(weeklyTimesheet);
                employees.merge(hoursLoggedByDay.resourceId(), employee, new Employee.mergeFunction());
            }

        );
        return employees.values().stream().sorted(new Employee.SortByName()).toList();
    }

    private List<HoursLoggedByDay> parseCsv() {
        List<HoursLoggedByDay> csvLines = new ArrayList<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(csvInputStream);
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                    .withSkipLines(1) // skip header
                    .withCSVParser(parser)
                    .build();

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                csvLines.add(new HoursLoggedByDay(
                        LocalDate.parse(nextRecord[0], ofPattern("dd/MM/yyyy")),
                        new ResourceId(nextRecord[1]),
                        nextRecord[2],
                        new WorkOrder(nextRecord[3]),
                        Double.parseDouble(nextRecord[4])
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csvLines;
    }

}
