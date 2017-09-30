package com.dragovorn.manipulator.command.executor;

import com.dragovorn.manipulator.Manipulator;

public class ExitExecutor implements CommandExecutor {
    @Override
    public void execute() {
        Manipulator.getInstance().shutdown();
    }

    @Override
    public boolean hasPermission() {
        return true;
    }
}