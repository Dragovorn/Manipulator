package com.dragovorn.manipulator.module;

import com.dragovorn.manipulator.log.ModuleLogger;
import com.dragovorn.manipulator.util.FileUtil;

import java.io.File;
import java.util.logging.Logger;

public class ManipulatorModule {

    private ModuleInfo info;

    private File moduleDirectory;

    private ModuleLogger logger;

    private ModuleLoader loader;

    public void onLoad() { }
    public void onEnable() { }
    public void onDisable() { }

    final void init(ModuleInfo info, ModuleLoader loader) {
        this.info = info;
        this.loader = loader;
        this.logger = new ModuleLogger(this);
        this.moduleDirectory = new File(FileUtil.modules, info.getName());
    }

    public Logger getLogger() {
        return this.logger;
    }

    public ModuleInfo getInfo() {
        return this.info;
    }

    public File getModuleDirectory() {
        return this.moduleDirectory;
    }

    ModuleLoader getLoader() {
        return this.loader;
    }
}