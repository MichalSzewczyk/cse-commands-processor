package com.mszewczyk.cp.service.commands;

import com.mszewczyk.cp.model.Command;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

// TODO: Implement command consumer
@Slf4j
public class StubbedConsumer implements Consumer<Command> {
    @Override
    public void accept(Command command) {
        log.info("Command received. [command={}]", command);
    }
}
