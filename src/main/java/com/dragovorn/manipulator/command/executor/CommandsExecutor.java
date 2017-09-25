package com.dragovorn.manipulator.command.executor;

import com.dragovorn.manipulator.Manipulator;

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