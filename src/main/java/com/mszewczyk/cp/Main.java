package com.mszewczyk.cp;

import com.mszewczyk.cp.configuration.yaml.JacksonMapDeserializer;
import com.mszewczyk.cp.configuration.yaml.JsonConfiguration;
import com.mszewczyk.cp.controller.CommandController;
import com.mszewczyk.cp.controller.CommandSource;
import com.mszewczyk.cp.controller.configuration.ControllerConfiguration;
import com.mszewczyk.cp.controller.routes.RequestHandler;
import com.mszewczyk.cp.service.AppLogicRoot;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        JacksonMapDeserializer deserializer = new JacksonMapDeserializer();
        ControllerConfiguration configuration = new JsonConfiguration(Path.of("src\\main\\resources\\application.json"), deserializer);
        RequestHandler handler = new RequestHandler();
        CommandSource source = new CommandController(configuration,
                handler::handleAddToGroup, handler::removeFromGroup);
        source.initialize();

        AppLogicRoot appLogicRoot = AppLogicRoot
                .builder()
                .commandProducer(handler)
                .build();

        appLogicRoot.wire();
    }
}
