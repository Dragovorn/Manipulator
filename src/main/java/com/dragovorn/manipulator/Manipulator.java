package com.dragovorn.manipulator;

import com.dragovorn.manipulator.command.Command;
import com.dragovorn.manipulator.command.CommandManager;
import com.dragovorn.manipulator.command.console.CommandConsole;
import com.dragovorn.manipulator.command.console.executor.CommandsExecutor;
import com.dragovorn.manipulator.command.console.executor.ExitExecutor;
import com.dragovorn.manipulator.command.console.executor.VersionExecutor;
import com.dragovorn.manipulator.log.DragonLogger;
import com.dragovorn.manipulator.log.LoggingOutputStream;
import com.dragovorn.manipulator.util.FileUtil;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manipulator {

    private static Manipulator instance;

    private CommandManager commandManager;

    private Logger logger;

    private Version version;

    private static boolean running;

    Manipulator(boolean gui) {
        instance = this;
        running = true;

        FileUtil.createDirectories();

        this.version = new Version();

        this.logger = new DragonLogger("Manipulator", FileUtil.log + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "-%g.log", gui);

        System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));

        this.logger.info("Starting Manipulator v" + this.getVersion() + "...");

        this.commandManager = new CommandManager();
        this.logger.info("Registering built-in console commands...");
        this.commandManager.registerCommand(new CommandConsole(new ExitExecutor(), "exit"));
        this.commandManager.registerCommand(new CommandConsole(new VersionExecutor(), "version"));
        this.commandManager.registerCommand(new CommandConsole(new CommandsExecutor(), "commands"));
        this.logger.info("Built-in console commands registered!");

        this.logger.info("Loading Manipulator Modules...");

    }

    public void shutdown() {
        getLogger().info("Shutting down...");

        running = false;

        new Thread("Shutdown Thread") {

            @Override
            public void run() {
                for (Handler handler : getLogger().getHandlers()) {
                    handler.close();
                }

                System.exit(0);
            }
        }.start();
    }

    public Map<String, Command> getConsoleCommands() {
        return this.commandManager.getConsoleCommands();
    }

    public Map<String, Command> getGameCommands() {
        return this.commandManager.getGameCommands();
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String getVersion() {
        return this.version.getVersion();
    }

    public static boolean isRunning() {
        return running;
    }

    public static Manipulator getInstance() {
        return instance;
    }
}