package com.dragovorn.manipulator.command.executor;

import com.dragovorn.manipulator.Manipulator;

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
