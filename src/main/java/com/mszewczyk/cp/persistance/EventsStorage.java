package com.mszewczyk.cp.persistance;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandSource;
import com.mszewczyk.cp.service.eventstore.EventStore;

import java.util.function.Consumer;


// TODO: Implement events storage
public class EventsStorage implements EventStore, CommandSource {
    @Override
    public void subscribe(Consumer<Command> consumer) {

    }

    @Override
    public void start() {

    }

    @Override
    public void store(Command command) {

    }
}
