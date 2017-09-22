package com.dragovorn.manipulator.log;

import com.dragovorn.manipulator.log.gui.ConsoleWindow;

import java.io.IOException;
import java.util.logging.*;

public class DragonLogger extends Logger {

    private final LogDispatcher DISPATCHER = new LogDispatcher(this);

    public DragonLogger(String name, String filePattern, boolean gui) {
        super(name, null);
        setLevel(Level.ALL);

        Formatter formatter = new ConciseFormatter();

        try {
            FileHandler fileHandler = new FileHandler(filePattern, 1 << 28, 8, true);
            fileHandler.setFormatter(formatter);
            addHandler(fileHandler);

            if (gui) {
                JTextAreaHandler handler = new JTextAreaHandler(ConsoleWindow.getInstance().getConsole());
                handler.setLevel(Level.INFO);
                handler.setFormatter(formatter);
                addHandler(handler);
            } else {
                ConsoleHandler handler = new ConsoleHandler();
                handler.setLevel(Level.INFO);
                handler.setFormatter(formatter);
                addHandler(handler);
            }
        } catch (IOException exception) {
            System.err.println("Could not register logger!");
            exception.printStackTrace();
        }

        this.DISPATCHER.start();
    }

    @Override
    public void log(LogRecord record) {
        this.DISPATCHER.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
    }
}