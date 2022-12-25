package com.cegeka.horizon.camis.synctimesheet;

import com.cegeka.horizon.camis.synctimesheet.csv.HoursLoggedCsvReader;
import com.cegeka.horizon.camis.synctimesheet.service.CheckWorkOrderService;
import com.cegeka.horizon.camis.timesheet.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Component
public class OperationExecutor {
    public static final String CHECK_WORK_ORDERS = "checkWorkOrders";
    public static final String SYNC_TIMESHEETS = "syncTimesheets";
    @Value("${operation}")
    private String operation;
    @Value("${input}")
    private String inputCsvFile;
    @Autowired
    private CheckWorkOrderService checkWorkOrderService;

    public void run() throws Exception {
        List<Employee> employees = new HoursLoggedCsvReader(new FileInputStream(inputCsvFile)).readCsv();

        switch (operation){
            case CHECK_WORK_ORDERS:
                checkWorkOrderService.check(employees);
                break;
            case SYNC_TIMESHEETS:
                break;
        }
    }

}
