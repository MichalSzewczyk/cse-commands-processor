package com.mszewczyk.cp.service.commands;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.eventstore.EventStore;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class CommandConsumer implements Consumer<Command> {
    private final EventStore eventStore;
    private final CommandProducer<Command> producer;

    public CommandConsumer(EventStore eventStore, CommandProducer<Command> producer) {
        this.eventStore = eventStore;
        this.producer = producer;
    }

    @Override
    public void accept(Command command) {
        log.info("Command received. [command={}]", command);
        eventStore.store(command);
        producer.produce(command);
    }
}
