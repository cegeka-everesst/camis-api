package com.cegeka.horizon.camis.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkOrderTest {

    @Test
    public void givenValidWorkOrder_whenConstruct_thenCreated(){
        new WorkOrder("LMAC001.003");
    }

    @Test
    public void givenEmptyWorkOrder_whenConstruct_thenCreated(){
        new WorkOrder("");
    }

    @Test
    public void givenInvalidWorkOrder_whenConstruct_thenCreated(){
        assertThatThrownBy(() -> new WorkOrder("x$z")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenInvalidWorkOrderDoubleDot_whenConstruct_thenCreated(){
        assertThatThrownBy(() -> new WorkOrder("LMAC001..003")).isInstanceOf(IllegalArgumentException.class);
    }


}