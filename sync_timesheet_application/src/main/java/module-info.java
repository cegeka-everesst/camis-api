module com.cegeka.horizon.camis.sync_timesheet {
    exports com.cegeka.horizon.camis.sync_timesheet.csv;
    exports com.cegeka.horizon.camis.sync_timesheet.service;
    exports com.cegeka.horizon.camis.sync_timesheet.service.command;

    requires com.cegeka.horizon.camis.domain;
    requires com.cegeka.horizon.camis.timesheet;
    requires com.cegeka.horizon.camis.workorder;

    opens com.cegeka.horizon.camis.sync_timesheet.csv to spring.core, spring.beans;
    opens com.cegeka.horizon.camis.sync_timesheet.service to spring.core, spring.beans;
    opens com.cegeka.horizon.camis.sync_timesheet.service.command to spring.core, spring.beans;

    requires org.slf4j;
    requires com.opencsv;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.webflux;
    requires spring.web;
    requires reactor.netty.http;
    requires reactor.netty.core;
    requires io.netty.transport;
    requires io.netty.handler;
    requires org.threeten.extra;
    requires com.cegeka.horizon.camis.sync_logger;
    requires org.json;
    requires spring.webmvc;
    requires reactor.core;
}
