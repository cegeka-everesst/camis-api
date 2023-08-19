package com.cegeka.horizon.camis.sync_logger.service;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.sync_logger.model.syncresult.CamisWorkorderInfo;
import com.cegeka.horizon.camis.sync_logger.model.syncresult.*;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//TODO: check if SyncLoggerService can be replaced by a Sink subscriber
@Service
public class SyncLoggerService {
    private static final Logger logger = LoggerFactory.getLogger("SyncLoggerService");

    public SyncResult logAndAddSyncRecordWithOtherError(EmployeeIdentification employeeId, CamisWorkorderInfo recordData) {
        logger.error(recordData.message());
        return new OtherSyncError(employeeId, recordData);
    }

    public SyncResult logAndAddSyncRecordWithHoursMinimum(EmployeeIdentification employeeId, CamisWorkorderInfo recordData, double minimumHoursRequired) {
        logger.error(recordData.message());
        return new HoursMinimumSyncError(employeeId, recordData, minimumHoursRequired);
    }

    public SyncResult logAndAddSyncRecordWithNoAction(EmployeeIdentification employeeId, CamisWorkorderInfo recordData) {
        logger.info(recordData.message());
        return new NoActionSyncCorrect(employeeId, recordData);
    }

    public SyncResult logAndAddSyncRecordWithUpdate(EmployeeIdentification employeeId, CamisWorkorderInfo recordData, double hours) {
        if (recordData.message().contains("Updated timesheetLine of employee")) {
            logger.info(recordData.message());
        } else if (recordData.message().contains("Could not update timesheetLine of external employee")) {
            logger.warn(recordData.message());
        } else {
            logger.error(recordData.message());
        }
        return new UpdateTimesheetLineSyncError(employeeId, recordData, hours);
    }
}
