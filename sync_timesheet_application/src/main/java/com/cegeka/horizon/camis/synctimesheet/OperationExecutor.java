package com.cegeka.horizon.camis.synctimesheet;

import com.cegeka.horizon.camis.synctimesheet.csv.HoursLoggedCsvReader;
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

    public void run() throws Exception {
        List<Employee> employees = new HoursLoggedCsvReader(new FileInputStream(inputCsvFile)).readCsv();

        switch (operation){
            case CHECK_WORK_ORDERS:
                System.out.println(employees);
                break;
            case SYNC_TIMESHEETS:
                break;
        }
    }

    /*
     @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private WorkorderAccessService workorderAccessService;

    System.out.println(timesheetService.getTimesheetEntries(new ResourceId("I098816"), LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 12)));
    System.out.println(
                        workorderAccessService.isAccessAllowed(new ResourceId("I098816"),
                                new WorkOrder("LMAC000.003"),
                                LocalDate.of(2022, 12, 11))

                );
     */
}
