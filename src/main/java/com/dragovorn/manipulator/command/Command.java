package com.dragovorn.manipulator.command;

public abstract class Command {

    protected CommandExecutor executor;

    private String name;

    protected Command(CommandExecutor executor, String name) {
        this.executor = executor;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract void handle();
}