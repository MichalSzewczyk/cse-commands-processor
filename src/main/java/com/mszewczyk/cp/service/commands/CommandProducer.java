package com.mszewczyk.cp.service.commands;

public interface CommandProducer<T> {
    void produce(T value);
}
