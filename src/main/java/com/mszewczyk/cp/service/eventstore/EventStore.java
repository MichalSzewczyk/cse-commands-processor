package com.mszewczyk.cp.service.eventstore;

import com.mszewczyk.cp.model.Command;

public interface EventStore {
    void store(Command command);
}