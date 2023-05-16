module com.cegeka.horizon.camis.sync_logger {
    requires com.cegeka.horizon.camis.domain;
    requires org.slf4j;
    requires org.json;
    requires spring.beans;
    requires spring.context;
    exports com.cegeka.horizon.camis.sync_logger.model;
    exports com.cegeka.horizon.camis.sync_logger.service;
    exports com.cegeka.horizon.camis.sync_logger.model.syncrecord;
    exports com.cegeka.horizon.camis.sync_logger.model.data;
}