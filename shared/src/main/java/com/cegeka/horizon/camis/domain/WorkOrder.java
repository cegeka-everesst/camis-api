package com.cegeka.horizon.camis.domain;

import static com.cegeka.horizon.camis.domain.ResourceId.CAMIS_COMPLETED;

public record WorkOrder(String value) {

    // Codes met punt (LMAC001.003) en zonder punt (EDU) zijn allebei geldig
    private static final String VALID_FORMAT = "[a-zA-Z0-9-]*(\\.[a-zA-Z0-9]*)?";

    public WorkOrder {
        if (!value.matches(VALID_FORMAT) && !"".equals(value) && !value.matches(CAMIS_COMPLETED))
            throw new IllegalArgumentException("Invalid format work order <" + value + ">");
    }

    public static WorkOrder empty() {
        return new WorkOrder("");
    }

    public boolean isEmpty() {
        return empty().value.equals(this.value);
    }
}