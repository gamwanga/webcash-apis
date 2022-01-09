package com.micropay.webcash.services;

import com.micropay.webcash.config.SchemaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ug.ac.mak.java.logger.Log;

@Component("CoreServices")
public class WebcashControllerService implements ApplicationListener<ContextRefreshedEvent> {
    public static SchemaConfig schemaConfig;
    public static Log logHandler;

    @Autowired
    public void setSchemaConfig(SchemaConfig schemaConfig) {
        WebcashControllerService.schemaConfig = schemaConfig;
    }

    @Autowired
    public void setLogger(Log logHandler) {
        WebcashControllerService.logHandler = logHandler;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
