package com.cegeka.horizon.camis.domain;

public record WorkOrder (String value) {
    public WorkOrder {
        if (!value.matches("[a-zA-Z0-9]*\\.[a-zA-Z0-9]*") && !"".equals(value))
            throw new IllegalArgumentException("Invalid format work order <" + value +">");
    }

    public boolean isEmpty(){
        return "".equals(this.value);
    }
}
