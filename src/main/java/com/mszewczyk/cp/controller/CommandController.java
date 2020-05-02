package com.mszewczyk.cp.controller;

import com.mszewczyk.cp.controller.configuration.ControllerConfiguration;
import lombok.extern.slf4j.Slf4j;
import spark.Route;

import static spark.Spark.delete;
import static spark.Spark.put;

@Slf4j
public class CommandController implements CommandSource {
    private final ControllerConfiguration configuration;
    private final Route addToGroupRoute;
    private final Route removeFromGroupRoute;

    public CommandController(ControllerConfiguration configuration,
                             Route addToGroupRoute,
                             Route removeFromGroupRoute) {
        this.configuration = configuration;
        this.addToGroupRoute = addToGroupRoute;
        this.removeFromGroupRoute = removeFromGroupRoute;
    }

    @Override
    public void initialize() {
        log.info("Started initialization of command controller.");
        configureRoutes();
        log.info("Configuration of command controller finished.");
    }

    private void configureRoutes() {
        put(configuration.addToGroupPath(), addToGroupRoute);
        delete(configuration.removeFromGroupPath(), removeFromGroupRoute);
    }
}
