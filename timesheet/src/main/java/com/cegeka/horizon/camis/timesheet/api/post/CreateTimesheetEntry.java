package com.cegeka.horizon.camis.timesheet.api.post;

public record CreateTimesheetEntry (String resource_id,
                                    String status,
                                    String activity,
                                    String description,
                                    String project,
                                    String timecode,
                                    String workorder,
                                    String external_reference,
                                    String registration_date,
                                    String hour_count,
                                    String InvValue) {

    //no documentation on this, just has to be 5
    public static String DEFAULT_INV_VALUE = "5";

    //if left blank, automatically filled in
    public static final String DEFAULT_WORKORDER_DESCRIPTION = "";
    public static final String DEFAULT_WORKORDER_PROJECT = "";

    public static final String DEFAULT_EXTERNAL_REF = "";
}
