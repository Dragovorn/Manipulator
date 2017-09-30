package com.dragovorn.manipulator.command.executor;

public interface CommandExecutor {

    void execute();

    boolean hasPermission();
}