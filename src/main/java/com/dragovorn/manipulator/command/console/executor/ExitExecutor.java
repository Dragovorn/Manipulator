package com.dragovorn.manipulator.command.console.executor;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.command.CommandExecutor;

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