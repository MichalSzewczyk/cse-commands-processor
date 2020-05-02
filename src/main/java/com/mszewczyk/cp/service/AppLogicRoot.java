package com.mszewczyk.cp.service;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandProducer;
import com.mszewczyk.cp.service.commands.StubbedConsumer;
import lombok.Builder;

import java.util.function.Consumer;

@Builder
public class AppLogicRoot {
    private final CommandProducer commandProducer;

    private AppLogicRoot(CommandProducer commandProducer) {
        this.commandProducer = commandProducer;
    }

    public void wire() {
        Consumer<Command> consumer = new StubbedConsumer();
        commandProducer.subscribe(consumer);
    }
}
