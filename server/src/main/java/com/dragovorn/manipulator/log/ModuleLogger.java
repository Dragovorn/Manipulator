package com.dragovorn.manipulator.log;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.module.ManipulatorModule;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ModuleLogger extends Logger {

    private String prefix;

    public ModuleLogger(ManipulatorModule module) {
        super(module.getClass().getCanonicalName(), null);

        this.prefix = "[" + module.getInfo().getName() + "] ";

        setParent(Manipulator.getInstance().getLogger());
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage(this.prefix + record.getMessage());
        super.log(record);
    }
}