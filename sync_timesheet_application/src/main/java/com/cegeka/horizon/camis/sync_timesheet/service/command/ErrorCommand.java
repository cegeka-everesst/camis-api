package com.cegeka.horizon.camis.sync_timesheet.service.command;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.sync.logger.model.result.CamisWorkorderInfo;
import com.cegeka.horizon.camis.sync.logger.model.result.HoursInfo;
import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
import com.cegeka.horizon.camis.timesheet.TimesheetService;
import org.springframework.web.reactive.function.client.WebClient;

public class ErrorCommand implements SyncCommand {
    private final EmployeeIdentification employeeId;
    private final CamisWorkorderInfo camisWorkorderInfo;
    private final HoursInfo hoursInfo;

    public ErrorCommand(EmployeeIdentification employeeId,
                        CamisWorkorderInfo camisWorkorderInfo,
                        HoursInfo hoursInfo) {
        this.employeeId = employeeId;
        this.camisWorkorderInfo = camisWorkorderInfo;
        this.hoursInfo = hoursInfo;
    }

    public EmployeeIdentification employeeId() {
        return employeeId;
    }

    public CamisWorkorderInfo camisWorkorderInfo() {
        return camisWorkorderInfo;
    }

    public HoursInfo hoursInfo() {
        return hoursInfo;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public SyncResult execute(WebClient webClient, TimesheetService timesheetService) {
        throw new IllegalStateException("This should never have been executed");
    }

    @Override
    public String toString() {
        return this.camisWorkorderInfo.message();
    }
}
