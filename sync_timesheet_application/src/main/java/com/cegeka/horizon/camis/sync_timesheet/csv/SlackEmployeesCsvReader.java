package com.cegeka.horizon.camis.sync_timesheet.csv;

import com.cegeka.horizon.camis.timesheet.Employee;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SlackEmployeesCsvReader {

    public static final char SEPARATOR = '^';
    private final InputStream csvInputStream;

    public SlackEmployeesCsvReader(InputStream inputStream){
        this.csvInputStream = inputStream;
    }

    public List<Employee> readCsv(List<Employee> employees) {
        List<SlackEmployee> slackEmployees = parseCsv();
        return mapToSlackNameToEmployee(employees, slackEmployees);
    }

    private List<Employee> mapToSlackNameToEmployee(List<Employee> employees, List<SlackEmployee> slackEmployees) {
       slackEmployees.forEach(slackEmployee -> employees.forEach(employee -> {
           if (slackEmployee.camisId().matches(employee.resourceId().value())) {
               employee.setSlackName(slackEmployee.slackUserName());
           }
       }));
       return employees;
    }

    private List<SlackEmployee> parseCsv() {
        List<SlackEmployee> csvLines = new ArrayList<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(csvInputStream);
            CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
            CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                    .withSkipLines(1) // skip header
                    .withCSVParser(parser)
                    .build();

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                csvLines.add(new SlackEmployee(
                        nextRecord[0],
                        Boolean.getBoolean(nextRecord[1]),
                        Boolean.getBoolean(nextRecord[2]),
                        nextRecord[3],
                        nextRecord[4],
                        nextRecord[5],
                        nextRecord[6],
                        nextRecord[7]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csvLines;
    }

}
