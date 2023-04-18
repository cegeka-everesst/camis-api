module com.cegeka.horizon.camis.sync_logger {
    requires com.cegeka.horizon.camis.domain;
    requires spring.context;
    requires org.slf4j;
    exports com.cegeka.horizon.camis.sync_logger;
    exports com.cegeka.horizon.camis.sync_logger.records;
}