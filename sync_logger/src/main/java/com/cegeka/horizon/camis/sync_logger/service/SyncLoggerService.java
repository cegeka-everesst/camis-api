package com.cegeka.horizon.camis.sync_logger.service;

import com.cegeka.horizon.camis.domain.EmployeeIdentification;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.model.syncrecord.*;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SyncLoggerService {
    private static final Logger logger = LoggerFactory.getLogger("SyncLoggerService");

    public SyncRecord logAndAddSyncRecordWithOtherError(EmployeeIdentification employeeId, RecordData recordData) {
        logger.error(recordData.getMessage());
        return new OtherSyncError(employeeId, recordData);
    }

    public SyncRecord logAndAddSyncRecordWithHoursMinimum(EmployeeIdentification employeeId, RecordData recordData, double minimumHours) {
        logger.error(recordData.getMessage());
        return new HoursMinimumSyncError(employeeId, recordData, minimumHours);
    }

    public SyncRecord logAndAddSyncRecordWithNoAction(EmployeeIdentification employeeId, RecordData recordData) {
        logger.info(recordData.getMessage());
        return new NoActionSyncCorrect(employeeId, recordData);
    }

    public SyncRecord logAndAddSyncRecordWithUpdate(EmployeeIdentification employeeId, RecordData recordData, double hours) {
        if (recordData.getMessage().contains("Updated timesheetLine of employee")) {
            logger.info(recordData.getMessage());
        } else if (recordData.getMessage().contains("Could not update timesheetLine of external employee")) {
            logger.warn(recordData.getMessage());
        } else {
            logger.error(recordData.getMessage());
        }
        return new UpdateTimesheetLineSyncErrorAndCorrect(employeeId, recordData, hours);
    }
}
