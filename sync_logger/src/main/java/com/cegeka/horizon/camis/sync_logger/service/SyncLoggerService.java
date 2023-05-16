package com.cegeka.horizon.camis.sync_logger.service;

import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.model.syncrecord.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SyncLoggerService {
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    public SyncRecord logAndAddSyncRecordWithOtherError(EmployeeData employerData, RecordData recordData) {
        logger.error(recordData.getMessage());
        return new OtherSyncError(employerData, recordData);
    }

    public SyncRecord logAndAddSyncRecordWithHoursMinimum(EmployeeData employerData, RecordData recordData, double minimumHours) {
        logger.error(recordData.getMessage());
        return new HoursMinimumSyncError(employerData, recordData, minimumHours);
    }

    public SyncRecord logAndAddSyncRecordWithNoAction(EmployeeData employerData, RecordData recordData) {
        logger.info(recordData.getMessage());
        return new NoActionSyncCorrect(employerData, recordData);
    }

    public SyncRecord logAndAddSyncRecordWithUpdate(EmployeeData employerData, RecordData recordData, double hours) {
        if (recordData.getMessage().contains("Updated timesheetLine of employee")) {
            logger.info(recordData.getMessage());
        } else if (recordData.getMessage().contains("Could not update timesheetLine of external employee")) {
            logger.warn(recordData.getMessage());
        } else {
            logger.error(recordData.getMessage());
        }
        return new UpdateTimesheetLineSyncErrorAndCorrect(employerData, recordData, hours);
    }
}
