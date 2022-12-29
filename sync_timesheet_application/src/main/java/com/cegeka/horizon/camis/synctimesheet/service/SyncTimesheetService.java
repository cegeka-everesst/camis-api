package com.cegeka.horizon.camis.synctimesheet.service;

import com.cegeka.horizon.camis.timesheet.Employee;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.extra.LocalDateRange;

import java.util.List;

@Service
public class SyncTimesheetService {

    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    @Autowired
    private TimesheetService timesheetService;

    public void sync(List<Employee> inputEmployees) {
        inputEmployees.forEach(
                employee -> {
                    LocalDateRange timesheetDuration = employee.getTimesheetDuration();
                    Employee camisTimesheetEntries = timesheetService.getTimesheetEntries(employee.resourceId(), employee.name(), timesheetDuration.getStart(), timesheetDuration.getEnd());
                    logger.info("Retrieved Camis timesheet entries for {} with result {}", employee.name(),camisTimesheetEntries);
                }
            );
    }

}
