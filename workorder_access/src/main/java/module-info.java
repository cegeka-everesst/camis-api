module com.cegeka.horizon.camis.workorder {
    exports com.cegeka.horizon.camis.workorder;

    requires com.cegeka.horizon.camis.domain;

    opens com.cegeka.horizon.camis.workorder.api to spring.beans;
    opens com.cegeka.horizon.camis.workorder.api.model to com.fasterxml.jackson.databind;

    requires org.slf4j;
    requires spring.context;
    requires spring.webflux;
    requires spring.beans;
    requires spring.web;
    requires com.fasterxml.jackson.annotation;
}