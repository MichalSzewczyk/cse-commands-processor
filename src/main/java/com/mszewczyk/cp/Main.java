package com.mszewczyk.cp;

import com.mszewczyk.cp.configuration.yaml.JacksonMapDeserializer;
import com.mszewczyk.cp.configuration.yaml.JsonConfiguration;
import com.mszewczyk.cp.controller.ControllerConfiguration;
import com.mszewczyk.cp.controller.SparkRequestHandler;
import com.mszewczyk.cp.persistance.EventsStorage;
import com.mszewczyk.cp.service.AppLogicRoot;
import com.mszewczyk.cp.service.commands.CommandSource;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        JacksonMapDeserializer deserializer = new JacksonMapDeserializer();
        ControllerConfiguration configuration = new JsonConfiguration(Path.of("src\\main\\resources\\application.json"), deserializer);
        boolean isRecovery = Boolean.parseBoolean(args[0]);
        EventsStorage eventStore = new EventsStorage(isRecovery);
        CommandSource requestHandler;
        if (isRecovery) {
            requestHandler = eventStore;
        } else {
            requestHandler = new SparkRequestHandler(configuration);
        }

        AppLogicRoot appLogicRoot = AppLogicRoot
                .builder()
                .commandSource(requestHandler)
                .eventStore(eventStore)
                .build();

        appLogicRoot.wire();

        requestHandler.start();
    }
}
