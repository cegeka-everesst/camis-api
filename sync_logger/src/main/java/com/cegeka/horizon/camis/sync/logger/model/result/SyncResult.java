package com.cegeka.horizon.camis.sync.logger.model.result;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;

import static com.cegeka.horizon.camis.sync.logger.model.result.SyncResultType.*;


public record SyncResult(EmployeeIdentification employee, SyncResultType type, CamisWorkorderInfo workorderInfo, HoursInfo hoursInfo) {
    public static SyncResult otherSyncError(EmployeeIdentification employeeId,
                                            CamisWorkorderInfo workorderInfo,
                                            HoursInfo hoursInfo) {
        return new SyncResult(employeeId, OTHER_SYNC_ERROR, workorderInfo, hoursInfo);
    }

    public static SyncResult hoursMinimumSyncError(EmployeeIdentification employeeId,
                                                   CamisWorkorderInfo workorderInfo,
                                                   double inputHours) {
        return new SyncResult(employeeId, INSUFFICIENT_HOURS_ERROR, workorderInfo, HoursInfo.inputHours(inputHours));
    }

    public static SyncResult noActionSyncCorrect(EmployeeIdentification employeeId,
                                                 CamisWorkorderInfo workorderInfo,
                                                  double inputHours) {
        return new SyncResult(employeeId, NO_SYNC_REQUIRED, workorderInfo, HoursInfo.compare(inputHours,inputHours));
    }

    public static SyncResult updateTimesheetLineSyncError(EmployeeIdentification employeeId,
                                                          CamisWorkorderInfo workorderInfo,
                                                          double inputHours) {
        return new SyncResult(employeeId, UNABLE_TO_UPDATE_TIMESHEET_ERROR,
                workorderInfo, HoursInfo.inputHours(inputHours));
    }

    public static SyncResult success(EmployeeIdentification employeeId, CamisWorkorderInfo workorderInfo, double hours) {
        return new SyncResult(employeeId, SUCCESS, workorderInfo, HoursInfo.compare(hours, hours));
    }

    public static SyncResult warning(EmployeeIdentification employeeId, CamisWorkorderInfo workorderInfo, double hours) {
        return new SyncResult(employeeId, WARNING, workorderInfo, HoursInfo.inputHours(hours));
    }
}
