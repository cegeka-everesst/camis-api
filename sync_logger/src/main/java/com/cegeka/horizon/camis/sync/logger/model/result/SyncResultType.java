package com.cegeka.horizon.camis.sync.logger.model.result;

public enum SyncResultType {

    NO_SYNC_REQUIRED(false),
    WARNING (false),
    INSUFFICIENT_HOURS_ERROR(true),
    SUCCESS(false),
    OTHER_SYNC_ERROR(true),
    UNABLE_TO_UPDATE_TIMESHEET_ERROR(true);

    private final boolean problematic;

    SyncResultType(boolean problematic) {
        this.problematic = problematic;
    }

    public boolean isProblematic() {
        return problematic;
    }
}
