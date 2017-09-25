package com.dragovorn.manipulator.module;

import com.dragovorn.manipulator.util.FileUtil;

import java.io.File;

public class ManipulatorModule {

    private ModuleInfo info;

    private File moduleDirectory;

    private ModuleLoader loader;

    public void onLoad() { }
    public void onEnable() { }
    public void onDisable() { }

    final void init(ModuleInfo info, ModuleLoader loader) {
        this.info = info;
        this.loader = loader;
        this.moduleDirectory = new File(FileUtil.modules, info.getName());
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