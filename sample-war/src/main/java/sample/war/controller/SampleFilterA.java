package sample.war.controller;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SampleFilterA implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleFilterA.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("before #doFilter");
        filterChain.doFilter(req, resp);
        LOGGER.info("after #doFilter");
    }
}
