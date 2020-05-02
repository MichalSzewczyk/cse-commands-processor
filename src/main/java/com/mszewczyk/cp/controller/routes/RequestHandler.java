package com.mszewczyk.cp.controller.routes;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandProducer;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

// TODO: Implement handlers
public class RequestHandler implements CommandProducer {
    private final Collection<Consumer<Command>> commandConsumers;

    public RequestHandler() {
        commandConsumers = new LinkedList<>();
    }

    @Override
    public void subscribe(Consumer<Command> consumer) {
        commandConsumers.add(consumer);
    }

    public String handleAddToGroup(Request request, Response response) {
        return null;
    }

    public String removeFromGroup(Request request, Response response) {
        return null;
    }
}
