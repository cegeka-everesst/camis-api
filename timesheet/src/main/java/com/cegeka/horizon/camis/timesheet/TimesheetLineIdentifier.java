package com.cegeka.horizon.camis.timesheet;

public record TimesheetLineIdentifier(String value) {
    public static final TimesheetLineIdentifier TO_CREATE = new TimesheetLineIdentifier("");
    public static final TimesheetLineIdentifier ERROR_OCCURRED = new TimesheetLineIdentifier("ERROR_OCCURRED");
}
