module com.cegeka.horizon.camis.timesheet {
    exports com.cegeka.horizon.camis.timesheet;

    requires com.cegeka.horizon.camis.domain;

    opens com.cegeka.horizon.camis.timesheet.api to spring.beans;
    opens com.cegeka.horizon.camis.timesheet.api.model to com.fasterxml.jackson.databind;

    requires spring.context;
    requires spring.webflux;
    requires spring.beans;
    requires spring.web;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
}