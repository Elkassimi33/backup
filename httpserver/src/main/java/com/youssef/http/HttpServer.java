package com.youssef.http;

import com.youssef.http.config.Configuration;
import com.youssef.http.config.ConfigurationManager;
import com.youssef.http.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    public static void main(String[] args) {

        LOGGER.info("Starting HttpServer");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf= ConfigurationManager.getInstance().getCurrentConfiguration();
        LOGGER.info("Using port: "+conf.getPort());
        LOGGER.info("Using webroot: "+conf.getWebroot());
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(),conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}