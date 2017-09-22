package com.dragovorn.manipulator.command.console;

import com.dragovorn.manipulator.command.Command;
import com.dragovorn.manipulator.command.CommandExecutor;

public final class ConsoleCommand extends Command {

    public ConsoleCommand(CommandExecutor executor, String name) {
        super(executor, name);
    }

    @Override
    public void handle() {
        this.executor.execute();
    }
}