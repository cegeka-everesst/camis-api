package com.cegeka.horizon.camis.timesheet;

import java.util.Arrays;

public enum TimeCode {
    WORK_DAY("AD"),
    NO_ASSIGNMENT("ZZ"),
    CAO("CAO"),
    BF("BF");

    private final String value;

    TimeCode(String value) {
        this.value = value;
    }

    public static TimeCode map(String valueToMap) {
        return Arrays.stream(values()).filter(timeCode -> timeCode.value.equals(valueToMap)).findFirst().get();
    }

    public String value() {
        return value;
    }
}
