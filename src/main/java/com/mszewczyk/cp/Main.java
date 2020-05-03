package com.mszewczyk.cp;

import com.mszewczyk.cp.configuration.ApplicationConfiguration;
import com.mszewczyk.cp.configuration.yaml.JacksonMapDeserializer;
import com.mszewczyk.cp.configuration.yaml.JsonConfiguration;
import com.mszewczyk.cp.controller.SparkRequestHandler;
import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.persistance.CassandraConnector;
import com.mszewczyk.cp.persistance.DatabaseConnector;
import com.mszewczyk.cp.persistance.EventsStorage;
import com.mszewczyk.cp.producer.KafkaEventsProducer;
import com.mszewczyk.cp.service.AppLogicRoot;
import com.mszewczyk.cp.service.commands.CommandSource;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        JacksonMapDeserializer deserializer = new JacksonMapDeserializer();
        ApplicationConfiguration configuration = new JsonConfiguration(Path.of("src\\main\\resources\\application.json"), deserializer);
        boolean isRecovery;
        if (args.length == 0) {
            isRecovery = false;
        } else {
            isRecovery = Boolean.parseBoolean(args[0]);
        }


        DatabaseConnector<Command> databaseConnector = new CassandraConnector(configuration);
        EventsStorage eventStore = new EventsStorage(isRecovery, databaseConnector);
        CommandSource commandSource;
        if (isRecovery) {
            commandSource = eventStore;
        } else {
            commandSource = new SparkRequestHandler(configuration);
        }

        AppLogicRoot appLogicRoot = AppLogicRoot
                .builder()
                .commandSource(commandSource)
                .eventStore(eventStore)
                .producer(new KafkaEventsProducer())
                .build();

        appLogicRoot.wire();

        databaseConnector.initializeDataStructure();
        commandSource.start();
    }
}
