package com.mszewczyk.cp.persistance;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandSource;
import com.mszewczyk.cp.service.eventstore.EventStore;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;


// TODO: Implement events storage
@Slf4j
public class EventsStorage implements EventStore, CommandSource {
    private final boolean isRecovery;
    private final Collection<Consumer<Command>> consumers;

    public EventsStorage(boolean isRecovery) {
        this.isRecovery = isRecovery;
        consumers = new LinkedList<>();
    }

    @Override
    public void subscribe(Consumer<Command> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void start() {

    }

    @Override
    public void store(Command command) {
        if (isRecovery) {
            log.info("System is in recovery state. Ignoring command.");
        } else {
            storeCommand(command);
        }
    }

    private void storeCommand(Command command) {

    }
}
