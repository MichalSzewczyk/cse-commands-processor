package com.mszewczyk.cp.model;

import lombok.ToString;

// TODO: extract to separate lib
@ToString
public class Command {
    private final Operation operation;
    private final String user;
    private final String group;

    public Command(Operation operation, String user, String group) {
        this.operation = operation;
        this.user = user;
        this.group = group;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getUser() {
        return user;
    }

    public String getGroup() {
        return group;
    }

    public enum Operation {
        ADD, REMOVE
    }
}
