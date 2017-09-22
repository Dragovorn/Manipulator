package com.dragovorn.manipulator.command.console.executor;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.command.CommandExecutor;

public class CommandsExecutor implements CommandExecutor {
    @Override
    public void execute() {
        Manipulator.getInstance().getLogger().info("Listing commands:");

        for (String str : Manipulator.getInstance().getGameCommands().keySet()) {
            Manipulator.getInstance().getLogger().info(str);
        }
    }

    @Override
    public boolean hasPermission() {
        return true;
    }
}