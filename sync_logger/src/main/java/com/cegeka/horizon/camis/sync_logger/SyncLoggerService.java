package com.cegeka.horizon.camis.sync_logger;

import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync_logger.records.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SyncLoggerService {
    private SyncList syncList = new SyncList();
    private static final Logger logger = LoggerFactory.getLogger("SyncTimesheets");

    public void logAndAddSyncRecord(String message, String employee) {
        logger.error(message);
        this.syncList.addSyncRecord(new OtherSyncError(message, employee));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate date) {
        this.syncList.addSyncRecord(new HoursNotEqualSyncError(message, employeeName, date));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate startDate, double minimumHours) {
        logger.error(message);
        this.syncList.addSyncRecord(new HoursMinimumSyncError(message, employeeName, startDate, minimumHours));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate date, WorkOrder workOrder) {
        logger.info(message);
        this.syncList.addSyncRecord(new NoActionSyncCorrect(message, employeeName, date, workOrder));
    }

    public void logAndAddSyncRecord(String message, String employeeName, LocalDate date, double hours, WorkOrder workOrder) {
        if (message.contains("Updated timesheetLine of employee")) { logger.info(message); }
        else if (message.contains("Could not update timesheetLine of external employee")) { logger.warn(message); }
        else { logger.error(message); }
        this.syncList.addSyncRecord(new UpdateTimsheetlineSyncErrorAndCorrect(message, employeeName, date, hours, workOrder));
    }

    public List<SyncRecord> getSyncListRecords() {
        return syncList.getSyncRecords();
    }
}
