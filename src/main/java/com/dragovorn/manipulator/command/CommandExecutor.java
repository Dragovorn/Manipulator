package com.dragovorn.manipulator.command;

public interface CommandExecutor {

    void execute();

    boolean hasPermission();
}