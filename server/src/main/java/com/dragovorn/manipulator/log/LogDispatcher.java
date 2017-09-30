package com.dragovorn.manipulator.log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

public class LogDispatcher extends Thread {

    private final DragonLogger logger;

    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

    public LogDispatcher(DragonLogger logger) {
        super("Logger Thread");
        this.logger = logger;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            LogRecord record;

            try {
                record = queue.take();
            } catch (InterruptedException exception) {
                continue;
            }

            logger.doLog(record);
        }

        for (LogRecord record : queue) {
            logger.doLog(record);
        }
    }

    public void queue(LogRecord record) {
        if (!isInterrupted()) {
            queue.add(record);
        }
    }
}