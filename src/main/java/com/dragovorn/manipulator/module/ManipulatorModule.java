package com.dragovorn.manipulator.module;

import com.dragovorn.manipulator.util.FileUtil;

import java.io.File;

public class ManipulatorModule {

    private ModuleInfo info;

    private File moduleDirectory;

    public void onLoad() { }
    public void onEnable() { }
    public void onDisable() { }

    final void init(ModuleInfo info) {
        this.info = info;
        this.moduleDirectory = new File(FileUtil.modules, info.getName());
    }

    public ModuleInfo getInfo() {
        return this.info;
    }

    public File getModuleDirectory() {
        return this.moduleDirectory;
    }
}