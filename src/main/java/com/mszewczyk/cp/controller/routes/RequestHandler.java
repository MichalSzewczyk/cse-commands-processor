package com.mszewczyk.cp.controller.routes;

import com.mszewczyk.cp.model.Command;
import com.mszewczyk.cp.service.commands.CommandProducer;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public class RequestHandler implements CommandProducer {
    private static final String USER_PATH_VARIABLE = ":user";
    private static final String GROUP_PATH_VARIABLE = ":group";
    private static final int ACCEPTED_STATUS_CODE = 202;
    private final Collection<Consumer<Command>> commandConsumers;

    public RequestHandler() {
        commandConsumers = new LinkedList<>();
    }

    @Override
    public void subscribe(Consumer<Command> consumer) {
        commandConsumers.add(consumer);
    }

    public String handleAddToGroup(Request request, Response response) {
        processRequest(request, response, Command.Operation.ADD);
        return "Added.";
    }

    public String removeFromGroup(Request request, Response response) {
        processRequest(request, response, Command.Operation.REMOVE);
        return "Removed.";
    }

    private void processRequest(Request request, Response response, Command.Operation add) {
        Command addCommand = extractCommand(request, add);
        populateToAllConsumers(addCommand);
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
