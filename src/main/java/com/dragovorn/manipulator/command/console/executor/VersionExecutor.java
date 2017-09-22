package com.dragovorn.manipulator.command.console.executor;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.command.CommandExecutor;

public class VersionExecutor implements CommandExecutor {
    @Override
    public void execute() {
        Manipulator.getInstance().getLogger().info("Running Manipulator v" + Manipulator.getInstance().getVersion() + "!");
    }

    @Override
    public boolean hasPermission() {
        return true;
    }
}
