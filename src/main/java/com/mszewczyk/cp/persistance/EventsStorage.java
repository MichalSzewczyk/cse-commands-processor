package com.mszewczyk.cp.persistance;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandSource;
import com.mszewczyk.cp.service.eventstore.EventStore;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

@Slf4j
public class EventsStorage implements EventStore, CommandSource {
    private final boolean isRecovery;
    private final DatabaseConnector<Command> databaseConnector;
    private final Collection<Consumer<Command>> consumers;

    public EventsStorage(boolean isRecovery, DatabaseConnector<Command> databaseConnector) {
        this.isRecovery = isRecovery;
        this.databaseConnector = databaseConnector;
        consumers = new LinkedList<>();
    }

    @Override
    public void subscribe(Consumer<Command> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void start() {
        databaseConnector.getAll().forEach(command -> consumers.forEach(consumer -> consumer.accept(command)));
        try {
            databaseConnector.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to close database connector.", e);
        }
    }

    @Override
    public void store(Command command) {
        if (isRecovery) {
            log.info("System is in recovery state. Ignoring command.");
        } else {
            databaseConnector.store(command);
        }
    }
}
