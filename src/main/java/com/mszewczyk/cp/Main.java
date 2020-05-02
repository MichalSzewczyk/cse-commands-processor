package com.mszewczyk.cp;

import com.mszewczyk.cp.configuration.yaml.JacksonMapDeserializer;
import com.mszewczyk.cp.configuration.yaml.JsonConfiguration;
import com.mszewczyk.cp.controller.CommandController;
import com.mszewczyk.cp.controller.CommandSource;
import com.mszewczyk.cp.controller.configuration.ControllerConfiguration;
import spark.Route;

import java.nio.file.Path;

public class Main {

    public static final Route ROUTE = ((request, response) -> "OK");

    public static void main(String[] args) {
        JacksonMapDeserializer deserializer = new JacksonMapDeserializer();
        ControllerConfiguration configuration = new JsonConfiguration(Path.of("src\\main\\resources\\application.json"), deserializer);
        //TODO: Replace with non stub routes
        CommandSource source = new CommandController(configuration, ROUTE, ROUTE);
        source.initialize();
    }
}
