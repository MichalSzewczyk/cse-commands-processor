package com.mszewczyk.cp.service.commands;

import com.mszewczyk.cp.model.Command;

import java.util.function.Consumer;

public interface CommandProducer {
    void subscribe(Consumer<Command> consumer);
}
