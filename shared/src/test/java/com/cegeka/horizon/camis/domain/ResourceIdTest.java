package com.cegeka.horizon.camis.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceIdTest {

    @Test
    public void givenValidInternalResourceId_whenIsValid_thenTrue(){
        new ResourceId("9004119");
        new ResourceId("1000616");
    }

    @Test
    public void givenInvalidInternalResourceId_whenIsValid_thenFalse(){
        assertThatThrownBy(() -> new ResourceId("noCamisId")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ResourceId("90041190")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenValidExternalResourceId_whenIsValid_thenTrue(){
        new ResourceId("I190401");
        new ResourceId("I099477");
    }

    @Test
    public void givenInvalidExternalResourceId_whenIsValid_thenFalse(){
        assertThatThrownBy(() -> new ResourceId("I0994770")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ResourceId("E099477")).isInstanceOf(IllegalArgumentException.class);
    }

}