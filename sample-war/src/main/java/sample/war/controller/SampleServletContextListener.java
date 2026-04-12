package sample.war.controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleServletContextListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("servlet context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("servlet context destroyed");
    }
}
