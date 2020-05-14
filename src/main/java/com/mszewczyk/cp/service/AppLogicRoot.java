package com.mszewczyk.cp.service;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandSource;
import com.mszewczyk.cp.service.commands.CommandConsumer;
import com.mszewczyk.cp.service.commands.CommandProducer;
import com.mszewczyk.cp.service.eventstore.EventStore;
import lombok.Builder;

import java.util.function.Consumer;

@Builder
public class AppLogicRoot {
    private final CommandSource commandSource;
    private final EventStore eventStore;
    private final CommandProducer<Command> producer;

    private AppLogicRoot(CommandSource commandSource,
                         EventStore eventStore,
                         CommandProducer<Command> producer) {
        this.commandSource = commandSource;
        this.eventStore = eventStore;
        this.producer = producer;
    }

    public void wire() {
        Consumer<Command> consumer = new CommandConsumer(eventStore, producer);
        commandSource.subscribe(consumer);
    }
}
