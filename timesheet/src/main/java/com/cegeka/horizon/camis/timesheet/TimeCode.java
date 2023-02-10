package com.cegeka.horizon.camis.timesheet;

public record TimeCode (String value){
    public static TimeCode WORK_DAY = new TimeCode("AD");
    public static TimeCode NO_ASSIGNMENT = new TimeCode("ZZ");
    public static TimeCode CAO = new TimeCode("CAO");
    public static TimeCode BF = new TimeCode("BF");
    public static TimeCode ON_CALL = new TimeCode("ACE");

}
