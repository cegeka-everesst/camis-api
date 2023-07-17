package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.model.syncrecord.SyncRecord;
import com.cegeka.horizon.camis.sync_logger.service.SyncLoggerService;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

public class NothingToSyncCommand implements SyncCommand {
    private final EmployeeData employerData;
    private final WorkOrder workOrder;
    private final LocalDate date;

    public NothingToSyncCommand(EmployeeData employerData, WorkOrder workOrder, LocalDate date) {
        this.employerData = employerData;
        this.workOrder = workOrder;
        this.date = date;
    }

    @Override
    public SyncRecord execute(WebClient webClient, TimesheetService timesheetService, SyncLoggerService syncLoggerService) {
        return syncLoggerService.logAndAddSyncRecordWithNoAction(employerData, new RecordData(date,"No Sync Action needed for " + employerData.getEmployeeName() + " on workOrder " + workOrder.value() + " and date " + date, workOrder));
    }
}
