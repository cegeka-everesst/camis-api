module com.cegeka.horizon.camis.synctimesheet {
    exports com.cegeka.horizon.camis.synctimesheet;

    requires com.cegeka.horizon.camis.domain;
    requires com.cegeka.horizon.camis.timesheet;
    requires com.cegeka.horizon.camis.workorder;

    opens com.cegeka.horizon.camis.synctimesheet to spring.core;


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
}
