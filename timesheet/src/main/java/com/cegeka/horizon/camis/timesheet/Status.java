package com.cegeka.horizon.camis.timesheet;

import java.util.Arrays;

public enum Status {
    TRANSFERRED("T"),
    DRAFT("P"),
    C("C"),
    READY("N");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status map(String valueToMap) {
        return Arrays.stream(values()).filter(status -> status.value.equals(valueToMap)).findFirst().get();
    }

    public boolean canBeDeleted(){
        return Arrays.asList(TRANSFERRED, READY).contains(this);
    }

    public String value() {
        return this.value;
    }
}
