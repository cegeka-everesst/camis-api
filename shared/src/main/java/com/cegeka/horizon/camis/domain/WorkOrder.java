package com.cegeka.horizon.camis.domain;

import static com.cegeka.horizon.camis.domain.ResourceId.CAMIS_COMPLETED;

public record WorkOrder(String value) {

    public WorkOrder {
        if (!value.matches("[a-zA-Z0-9-]*\\.[a-zA-Z0-9]*") && !"".equals(value) && !value.matches(CAMIS_COMPLETED))
            throw new IllegalArgumentException("Invalid format work order <" + value + ">");
    }

    public static WorkOrder empty() {
        return new WorkOrder("");
    }

    public boolean isEmpty() {
        return empty().value.equals(this.value);
    }
}
