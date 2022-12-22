package com.cegeka.horizon.camis.domain;

public class ResourceId {
    private String value;

    public ResourceId(String value) {
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
