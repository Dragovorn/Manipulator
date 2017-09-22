package com.dragovorn.manipulator.command.game;

import com.dragovorn.manipulator.command.Command;
import com.dragovorn.manipulator.command.CommandExecutor;

public final class CommandGame extends Command {

    public CommandGame(CommandExecutor executor, String name) {
        super(executor, name);
    }

    @Override
    public void handle() {
        if (this.executor.hasPermission()) {
            this.executor.execute();
        }
    }
}