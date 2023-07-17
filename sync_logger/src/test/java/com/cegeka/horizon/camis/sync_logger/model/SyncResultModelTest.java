package com.cegeka.horizon.camis.sync_logger.model;

import com.cegeka.horizon.camis.domain.ResourceId;
import com.cegeka.horizon.camis.domain.WorkOrder;
import com.cegeka.horizon.camis.sync_logger.model.data.EmployeeData;
import com.cegeka.horizon.camis.sync_logger.model.data.RecordData;
import com.cegeka.horizon.camis.sync_logger.model.syncrecord.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SyncResultModelTest {
    private SyncResult syncResult;
    private List<SyncDay> syncDays;

    @BeforeEach
    public void setUp() {
        syncResult = new SyncResult();
        syncDays = new ArrayList<>();
    }

    @Test
    public void givenNewSyncResult_whenGetSyncRecordsFromSyncResult_thenReturnEmptyList() {
        assertTrue(syncResult.getSyncRecords().isEmpty());
    }

    @Test
    public void givenNewSyncResult_whenGetSyncDaysFromSyncResult_thenReturnEmptyList() {
        assertTrue(syncResult.getSyncDays().isEmpty());
    }

    @Test
    public void givenCorrectHoursMinimumSyncErrorRecord_whenAddSyncResult_thenAddedRecordToSyncResult() {
        HoursMinimumSyncError hoursMinimumSyncErrorRecord = new HoursMinimumSyncError(new EmployeeData(new ResourceId("I000001"), "Employee1", "SlackEmployee1"), new RecordData(LocalDate.now(), "Message 1", new WorkOrder("001.001")), 40);
        syncResult.addSyncRecord(hoursMinimumSyncErrorRecord);
        assertEquals(hoursMinimumSyncErrorRecord, syncResult.getSyncRecords().get(0));
    }

    @Test
    public void givenCorrectHoursNotEqualSyncErrorRecord_whenAddSyncResult_thenAddedRecordToSyncResult() {
        HoursNotEqualSyncError hoursNotEqualSyncErrorRecord = new HoursNotEqualSyncError(new EmployeeData(new ResourceId("I000002"), "Employee2", "SlackEmployee2"), new RecordData(LocalDate.now(), "Message 2", new WorkOrder("002.002")));
        syncResult.addSyncRecord(hoursNotEqualSyncErrorRecord);
        assertEquals(hoursNotEqualSyncErrorRecord, syncResult.getSyncRecords().get(0));
    }

    @Test
    public void givenCorrectNoActionSyncRecord_whenAddSyncResult_thenAddedRecordToSyncResult() {
        NoActionSyncCorrect noActionSyncCorrectRecord = new NoActionSyncCorrect(new EmployeeData(new ResourceId("I000003"), "Employee3", "SlackEmployee3"), new RecordData(LocalDate.now(), "Message 3", new WorkOrder("003.003")));
        syncResult.addSyncRecord(noActionSyncCorrectRecord);
        assertEquals(noActionSyncCorrectRecord, syncResult.getSyncRecords().get(0));
    }

    @Test
    public void givenCorrectOtherSyncErrorRecord_whenAddSyncResult_thenAddedRecordToSyncResult() {
        OtherSyncError otherSyncError = new OtherSyncError(new EmployeeData(new ResourceId("I000004"), "Employee4", "SlackEmployee4"), new RecordData(LocalDate.now(), "Message 4", new WorkOrder("004.004")));
        syncResult.addSyncRecord(otherSyncError);
        assertEquals(otherSyncError, syncResult.getSyncRecords().get(0));
    }

    @Test
    public void givenCorrectUpdateTimesheetLineSyncErrorAndCorrectRecord_whenAddSyncResult_thenAddedRecordToSyncResult() {
        UpdateTimesheetLineSyncErrorAndCorrect updateTimesheetLineSyncErrorAndCorrect = new UpdateTimesheetLineSyncErrorAndCorrect(new EmployeeData(new ResourceId("I000005"), "Employee5", "SlackEmployee5"), new RecordData(LocalDate.now(), "Message 3", new WorkOrder("005.005")), 40);
        syncResult.addSyncRecord(updateTimesheetLineSyncErrorAndCorrect);
        assertEquals(updateTimesheetLineSyncErrorAndCorrect, syncResult.getSyncRecords().get(0));
    }

    @Test
    public void givenCorrectSyncDays_whenAddSyncResult_thenAddedSyncDaysToSyncResult() {
        SyncDay syncDay1 = new SyncDay(new ResourceId("I000001"), LocalDate.now(), new WorkOrder("001.001"), 6, 7);
        SyncDay syncDay2 = new SyncDay(new ResourceId("I000002"), LocalDate.now(), new WorkOrder("002.002"), 7, 8);

        syncDays.add(syncDay1);
        syncDays.add(syncDay2);

        syncResult.addSyncDays(syncDays);
        assertEquals(syncDay1, syncResult.getSyncDays().get(0));
        assertEquals(syncDay2, syncResult.getSyncDays().get(1));
    }
}