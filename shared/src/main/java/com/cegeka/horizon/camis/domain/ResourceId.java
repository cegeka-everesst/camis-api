package com.cegeka.horizon.camis.domain;

public record ResourceId (String value) {

    public boolean isExternal(){
        return value.startsWith("I");
    }
}
