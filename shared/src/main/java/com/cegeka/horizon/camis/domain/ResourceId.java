package com.cegeka.horizon.camis.domain;

public record ResourceId (String value) {

    public boolean isExternal(String value){
        return value.startsWith("I");
    }
}
