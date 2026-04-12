package sample.war.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SampleServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getOutputStream().write("<h1>sample servlet</h1>".getBytes());
        LOGGER.info("processed");
    }
}
