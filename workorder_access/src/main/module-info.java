module com.cegeka.horizon.camis.workorder {
    exports com.cegeka.horizon.camis.workorder;

    requires com.cegeka.horizon.camis.domain;

    opens com.cegeka.horizon.camis.workorder.api to spring.beans;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires spring.context;
    requires spring.webflux;
    requires spring.beans;
    requires spring.web;
}