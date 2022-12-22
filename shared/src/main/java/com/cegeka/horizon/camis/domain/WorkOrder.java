package com.cegeka.horizon.camis.domain;

public class WorkOrder {
    private String value;

    public WorkOrder(String value){
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }
}
