package com.dragovorn.manipulator.command.console;

import com.dragovorn.manipulator.command.Command;
import com.dragovorn.manipulator.command.executor.CommandExecutor;

public final class CommandConsole extends Command {

    public CommandConsole(CommandExecutor executor, String name) {
        super(executor, name);
    }

    @Override
    public void handle() {
        this.executor.execute();
    }
}