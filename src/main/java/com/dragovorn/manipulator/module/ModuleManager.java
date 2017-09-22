package com.dragovorn.manipulator.module;

import com.dragovorn.manipulator.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModuleManager {

    private Map<String, ModuleInfo> load;

    private Map<String, Module> plugins;

    public ModuleManager() {
        this.load = new HashMap<>();
        this.plugins = new HashMap<>();
    }

    public void loadModules() {
        if (FileUtil.modules.listFiles() != null) {
            for (File file : FileUtil.modules.listFiles()) {
                if (!file.getName().matches("(.+).(jar)")) {
                    continue;
                }

                try {
                    ModuleInfo info = loadModuleInfo(file);

                    this.load.put(info.getName().toLowerCase(), info);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private ModuleInfo loadModuleInfo(File file) throws IOException {
        return null;
    }
}