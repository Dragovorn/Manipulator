package com.dragovorn.manipulator;

import com.dragovorn.manipulator.log.DragonLogger;
import com.dragovorn.manipulator.log.LoggingOutputStream;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manipulator {

    private Logger logger;

    private Version version;

    private static Manipulator instance;

    Manipulator(boolean gui) {
        instance = this;

        File logDir = new File("logs");

        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        this.version = new Version();
        this.logger = new DragonLogger("Manipulator", logDir + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "-%g.log", gui);

        System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));

        this.logger.info("LUL");
    }

    public String getVersion() {
        return this.version.getVersion();
    }

    public static Manipulator getInstance() {
        return instance;
    }
}