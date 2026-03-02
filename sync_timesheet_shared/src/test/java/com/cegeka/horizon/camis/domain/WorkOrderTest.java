package com.cegeka.horizon.camis.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkOrderTest {

    @Test
    void givenValidWorkOrder_whenConstruct_thenCreated() {
        new WorkOrder("LMAC001.003");
    }

    @Test
    void givenValidWorkOrderCanContainDashes_whenConstruct_thenCreated() {
        new WorkOrder("PZ--001.001");
    }

    @Test
    void givenEmptyWorkOrder_whenConstruct_thenCreated() {
        new WorkOrder("");
    }

    @Test
    void givenInvalidWorkOrder_whenConstruct_thenCreated() {
        assertThatThrownBy(() -> new WorkOrder("x$z")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenInvalidWorkOrderDoubleDot_whenConstruct_thenCreated() {
        assertThatThrownBy(() -> new WorkOrder("LMAC001..003")).isInstanceOf(IllegalArgumentException.class);
    }
}