package com.cegeka.horizon.camis.synctimesheet;

import com.cegeka.horizon.camis.synctimesheet.csv.HoursLoggedCsvReader;
import com.cegeka.horizon.camis.synctimesheet.service.CheckWorkOrderService;
import com.cegeka.horizon.camis.synctimesheet.service.SyncTimesheetService;
import com.cegeka.horizon.camis.timesheet.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.List;

@Component
public class OperationExecutor {
    @Value("${operation}")
    private String operation;
    @Value("${input}")
    private String inputCsvFile;
    @Autowired
    private CheckWorkOrderService checkWorkOrderService;
    @Autowired
    private SyncTimesheetService syncTimesheetService;

    enum Operation{
        CHECK_WORK_ORDERS,
        SYNC_TIMESHEETS,
        VIEW_TIMESHEETS
    }

    public void run() throws Exception {
        List<Employee> employees = new HoursLoggedCsvReader(new FileInputStream(inputCsvFile)).readCsv();

        switch (Operation.valueOf(operation)) {
            case CHECK_WORK_ORDERS -> checkWorkOrderService.check(employees);
            case VIEW_TIMESHEETS -> syncTimesheetService.retrieve(employees);
            case SYNC_TIMESHEETS -> syncTimesheetService.sync(employees);
        }
    }

}
