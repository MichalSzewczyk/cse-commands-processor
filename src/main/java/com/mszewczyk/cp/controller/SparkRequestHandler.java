package com.mszewczyk.cp.controller;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandSource;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import static spark.Spark.delete;
import static spark.Spark.put;

@Slf4j
public class SparkRequestHandler implements CommandSource {
    private static final String USER_PATH_VARIABLE = ":user";
    private static final String GROUP_PATH_VARIABLE = ":group";
    private static final int ACCEPTED_STATUS_CODE = 202;
    private final Collection<Consumer<Command>> commandConsumers;
    private final ControllerConfiguration configuration;

    public SparkRequestHandler(ControllerConfiguration configuration) {
        this.configuration = configuration;
        commandConsumers = new LinkedList<>();
        log.info("Initialized request handler.");
    }

    @Override
    public void start() {
        log.info("Started initialization of command controller.");
        configureRoutes();
        log.info("Configuration of command controller finished.");
    }

    @Override
    public void subscribe(Consumer<Command> consumer) {
        commandConsumers.add(consumer);
    }

    private void configureRoutes() {
        put(configuration.addToGroupPath(), this::handleAddToGroup);
        delete(configuration.removeFromGroupPath(), this::removeFromGroup);
    }

    private String handleAddToGroup(Request request, Response response) {
        log.info("Processing request to add to group. [request={}]", request);
        processRequest(request, response, Command.Operation.ADD);
        return "Added.";
    }

    private String removeFromGroup(Request request, Response response) {
        log.info("Processing request to remove from group. [request={}]", request);
        processRequest(request, response, Command.Operation.REMOVE);
        return "Removed.";
    }

    private void processRequest(Request request, Response response, Command.Operation add) {
        Command command = extractCommand(request, add);
        log.info("Processing command. [command={}]", command);
        populateToAllConsumers(command);
        response.status(ACCEPTED_STATUS_CODE);
    }

    private Command extractCommand(Request request, Command.Operation operation) {
        String user = request.params(USER_PATH_VARIABLE);
        String group = request.params(GROUP_PATH_VARIABLE);
        return new Command(operation, user, group);
    }

    private void populateToAllConsumers(Command command) {
        commandConsumers.forEach(consumer -> consumer.accept(command));
    }
}
