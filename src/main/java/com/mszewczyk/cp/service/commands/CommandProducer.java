package com.mszewczyk.cp.service.commands;

import com.mszewczyk.cp.model.Command;

public interface CommandProducer {
    void produce(Command command);
}
