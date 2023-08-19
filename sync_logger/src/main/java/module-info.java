module com.cegeka.horizon.camis.sync_logger {
    requires com.cegeka.horizon.camis.domain;
    requires org.slf4j;
    requires org.json;
    requires spring.beans;
    requires spring.context;
    exports com.cegeka.horizon.camis.sync.logger.service;
    exports com.cegeka.horizon.camis.sync.logger.model.result;
}