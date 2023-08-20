package com.cegeka.horizon.camis.sync.logger.service;

import com.cegeka.horizon.camis.sync.logger.model.result.SyncResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncLoggerService {
    private static final Logger logger = LoggerFactory.getLogger("SyncLoggerService");

    public void log(SyncResult syncResult) {
        switch (syncResult.type()){
            case SUCCESS -> logger.info(syncResult.workorderInfo().message());
            case WARNING -> logger.warn(syncResult.workorderInfo().message());
            default -> logger.error(syncResult.workorderInfo().message());
        }
    }
}
