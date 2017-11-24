package com.dragovorn.manipulator.command;

import com.dragovorn.manipulator.command.console.CommandConsole;
import com.dragovorn.manipulator.command.executor.CommandExecutor;
import com.dragovorn.manipulator.command.game.CommandGame;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Ignore
@RunWith(DataProviderRunner.class)
public class TestCommandManager {

    private CommandManager manager;

    @Before
    public void setup() {
        this.manager = new CommandManager();
    }

    @Test
    @UseDataProvider("registerData")
    public void testRegister(Command command, boolean console, boolean exception) {
        try {
            this.manager.registerCommand(command);
        } catch (InvalidCommandTypeException e) {
            if (!exception) {
                fail();
            }
        }

        if (console) {
            assertEquals(1, this.manager.getConsoleCommands().size());
        } else if (!exception) {
            assertEquals(1, this.manager.getGameCommands().size());
        }
    }

    @DataProvider
    public static Object[][] registerData() {
        CommandExecutor executor = new CommandExecutor() {
            @Override
            public void execute() {

            }

            @Override
            public boolean hasPermission() {
                return false;
            }
        };

        return new Object[][] {
                { new CommandConsole(executor, "cmd"), true, false },
                { new CommandGame(executor, "cmd"), false, false },
                { new BadCommand(executor, "cmd"), false, true }
        };
    }

    static class BadCommand extends Command {

        BadCommand(CommandExecutor executor, String name) {
            super(executor, name);
        }

        @Override
        public void handle() {

        }
    }
}