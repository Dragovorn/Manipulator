package com.dragovorn.manipulator.command;

import com.dragovorn.manipulator.command.console.ConsoleCommand;
import com.dragovorn.manipulator.command.game.GameCommand;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private Map<String, Command> consoleCommands;
    private Map<String, Command> gameCommands;

    public CommandManager() {
        this.consoleCommands = new HashMap<>();
        this.gameCommands = new HashMap<>();
    }

    public void registerCommand(Command command) {
        if (command instanceof ConsoleCommand) {
            insertInto(this.consoleCommands, command);
        } else if (command instanceof GameCommand) {
            insertInto(this.gameCommands, command);
        } else {
            throw new InvalidCommandTypeException();
        }
    }

    public Map<String, Command> getConsoleCommands() {
        return ImmutableMap.copyOf(this.consoleCommands);
    }

    public Map<String, Command> getGameCommands() {
        return ImmutableMap.copyOf(this.gameCommands);
    }

    private void insertInto(Map<String, Command> map, Command command) {
        if (map.containsKey(command.getName())) {
            return;
        }

        map.put(command.getName(), command);
    }
}