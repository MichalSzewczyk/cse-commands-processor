package com.mszewczyk.cp;

import com.mszewczyk.cp.configuration.yaml.JacksonMapDeserializer;
import com.mszewczyk.cp.configuration.yaml.JsonConfiguration;
import com.mszewczyk.cp.controller.configuration.ControllerConfiguration;
import com.mszewczyk.cp.controller.SparkRequestHandler;
import com.mszewczyk.cp.persistance.EventsStorage;
import com.mszewczyk.cp.service.AppLogicRoot;
import com.mszewczyk.cp.service.commands.CommandSource;
import com.mszewczyk.cp.service.eventstore.EventStore;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        JacksonMapDeserializer deserializer = new JacksonMapDeserializer();
        ControllerConfiguration configuration = new JsonConfiguration(Path.of("src\\main\\resources\\application.json"), deserializer);
        CommandSource commandSource = new SparkRequestHandler(configuration);

        EventStore eventStore = new EventsStorage();
        AppLogicRoot appLogicRoot = AppLogicRoot
                .builder()
                .commandSource(commandSource)
                .eventStore(eventStore)
                .build();

        appLogicRoot.wire();

        commandSource.start();
    }
}
