package com.cegeka.horizon.camis.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class WorkOrderTest {

    @Test
    public void givenValidWorkOrder_whenConstruct_thenCreated(){
        new WorkOrder("LMAC001.003");
    }

    @Test
    public void givenInvalidWorkOrder_whenConstruct_thenCreated(){
        Assertions.assertThatThrownBy(() -> new WorkOrder("x$z")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenInvalidWorkOrderDoubleDot_whenConstruct_thenCreated(){
        Assertions.assertThatThrownBy(() -> new WorkOrder("LMAC001..003")).isInstanceOf(IllegalArgumentException.class);
    }

}