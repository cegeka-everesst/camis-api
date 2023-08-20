package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync.logger.model.result.CamisWorkorderInfo;
import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

public class NothingToSyncCommand implements SyncCommand {
    private final EmployeeIdentification employeeId;
    private final WorkOrder workOrder;
    private final LocalDate date;

    public NothingToSyncCommand(EmployeeIdentification employerData, WorkOrder workOrder, LocalDate date) {
        this.employeeId = employerData;
        this.workOrder = workOrder;
        this.date = date;
    }

    @Override
    public SyncResult execute(WebClient webClient, TimesheetService timesheetService) {
        return SyncResult.noActionSyncCorrect(employeeId, new CamisWorkorderInfo(date,"No Sync Action needed for " + employeeId.name() + " on workOrder " + workOrder.value() + " and date " + date, workOrder));
    }
}
