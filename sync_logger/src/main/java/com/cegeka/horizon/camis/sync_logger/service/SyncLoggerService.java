package com.cegeka.horizon.camis.sync_logger.service;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync_logger.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SyncLoggerService {
    private SyncResult syncResult = new SyncResult();
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    public void logAndAddSyncRecord(String message, String employee) {
        logger.error(message);
        this.syncResult.addSyncRecord(new OtherSyncError(message, employee));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate date) {
        this.syncResult.addSyncRecord(new HoursNotEqualSyncError(message, employeeName, date));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate startDate, double minimumHours) {
        logger.error(message);
        this.syncResult.addSyncRecord(new HoursMinimumSyncError(message, employeeName, startDate, minimumHours));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate date, WorkOrder workOrder) {
        logger.info(message);
        this.syncResult.addSyncRecord(new NoActionSyncCorrect(message, employeeName, date, workOrder));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate date, double hours, WorkOrder workOrder) {
        if (message.contains("Updated timesheetLine of employee")) {
            logger.info(message);
        } else if (message.contains("Could not update timesheetLine of external employee")) {
            logger.warn(message);
        } else {
            logger.error(message);
        }
        this.syncResult.addSyncRecord(new UpdateTimsheetLineSyncErrorAndCorrect(message, employeeName, date, hours, workOrder));
    }

    public List<SyncRecord> getSyncResultRecords() {
        return syncResult.getSyncRecords();
    }

    public SyncResult getSyncResult() {
        return this.syncResult;
    }
}
