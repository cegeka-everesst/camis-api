module com.cegeka.horizon.camis.timesheet {
    exports com.cegeka.horizon.camis.timesheet;

    requires com.cegeka.horizon.camis.domain;

    opens com.cegeka.horizon.camis.timesheet.api to spring.beans, spring.core;
    opens com.cegeka.horizon.camis.timesheet.api.get to com.fasterxml.jackson.databind, spring.beans;
    opens com.cegeka.horizon.camis.timesheet.api.post to com.fasterxml.jackson.databind, spring.beans;
    opens com.cegeka.horizon.camis.timesheet.api.delete to com.fasterxml.jackson.databind, spring.beans;


    requires spring.context;
    requires spring.webflux;
    requires spring.beans;
    requires spring.web;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires reactor.core;

    requires org.threeten.extra;
}