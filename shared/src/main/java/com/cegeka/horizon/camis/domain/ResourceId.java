package com.cegeka.horizon.camis.domain;

import java.util.Objects;

public class ResourceId {

    private final String value;

    public ResourceId(String value){
        if (! isValid(value)) throw new IllegalArgumentException("Invalid value for ResourceId : " + value);
        this.value = value;
    }

    private boolean isValid(String value) {
        return value.matches("I?\\d{6}") || value.matches("\\d{7}");
    }

    public boolean isExternal(){
        return value.startsWith("I");
    }


    public String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceId that = (ResourceId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
