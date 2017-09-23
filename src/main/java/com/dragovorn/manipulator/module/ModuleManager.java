package com.dragovorn.manipulator.module;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.module.asm.ModuleClassVisitor;
import com.dragovorn.manipulator.util.FileUtil;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

public class ModuleManager {

    private Map<String, ModuleInfo> load;

    private Map<String, ManipulatorModule> modules;

    public ModuleManager() {
        this.load = new HashMap<>();
        this.modules = new HashMap<>();
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

        Map<ModuleInfo, Boolean> pluginStatus = new HashMap<>();

        for (Map.Entry<String, ModuleInfo> entry : this.load.entrySet()) {
            if (!loadPlugin(pluginStatus, new Stack<>(), entry.getValue())) {
                Manipulator.getInstance().getLogger().log(Level.INFO, "Failed to enable {0}", entry.getKey());
            }
        }

        this.load.clear();
        this.load = null;
    }

    private boolean loadPlugin(Map<ModuleInfo, Boolean> pluginStatus, Stack<ModuleInfo> dependencies, ModuleInfo info) {
        if (pluginStatus.containsKey(info)) {
            return pluginStatus.get(info);
        }

        Set<String> depends = new HashSet<>();

        if (info.getDependencies().length != 0) {
            depends.addAll(Arrays.asList(info.getDependencies()));
        }

        boolean status = true;

        for (String name : depends) {
            ModuleInfo depend = this.load.get(name.toLowerCase());

            Boolean dependStatus = (depend != null) ? pluginStatus.get(depend) : Boolean.FALSE;

            if (dependStatus == null) {
                if (dependencies.contains(depend)) {
                    StringBuilder graph = new StringBuilder();

                    for (ModuleInfo pluginInfo : dependencies) {
                        graph.append(pluginInfo.getName()).append(" -> ");
                    }

                    graph.append(info.getName()).append(" -> ").append(name);
                    Manipulator.getInstance().getLogger().log(Level.WARNING, "Circular dependency detected {0}", graph);
                    status = false;
                } else {
                    dependencies.push(info);
                    dependStatus = this.loadPlugin(pluginStatus, dependencies, depend);
                    dependencies.pop();
                }
            }

            if (dependStatus == Boolean.FALSE) {
                Manipulator.getInstance().getLogger().log(Level.WARNING, "{0} (required by {1}) is not available!", new Object[] { name, info.getName() });
                status = false;
            }

            if (!status) {
                break;
            }
        }

        if (status) {
            try {
                URLClassLoader loader = new ModuleLoader(new URL[] {info.getFile().toURI().toURL()});

                Class<?> main = loader.loadClass(info.getMain());
                ManipulatorModule clazz = (ManipulatorModule) main.getDeclaredConstructor().newInstance();
                clazz.init(info);

                this.modules.put(info.getName(), clazz);
                clazz.onLoad();

                Manipulator.getInstance().getLogger().log(Level.INFO, "Loaded {0} version {1} by {2}!", new Object[] { info.getName(), info.getVersion(), info.getAuthor() });
            } catch (Throwable throwable) {
                return false;
            }
        }

        pluginStatus.put(info, status);

        return status;
    }

    public void enablePlugins() {
        for (ManipulatorModule plugin : this.modules.values()) {
            try {
                plugin.onEnable();

                Manipulator.getInstance().getLogger().log(Level.INFO, "Enabled {0} version {1} by {2}", new Object[] { plugin.getInfo().getName(), plugin.getInfo().getVersion(), plugin.getInfo().getName() });
            } catch (Throwable throwable) {
                Manipulator.getInstance().getLogger().log(Level.WARNING, "Encountered exception while enabling module: " + plugin.getInfo().getName(), throwable);
            }
        }
    }

    public void disablePlugins() {
        for (ManipulatorModule plugin : this.modules.values()) {
            try {
                plugin.onDisable();

                Manipulator.getInstance().getLogger().log(Level.INFO, "Disabled {0} version {1} by {2}", new Object[] { plugin.getInfo().getName(), plugin.getInfo().getVersion(), plugin.getInfo().getAuthor() });
            } catch (Throwable throwable) {
                Manipulator.getInstance().getLogger().log(Level.WARNING, "Encountered exception while disabling module: " + plugin.getInfo().getName(), throwable);
            }
        }
    }

    public static ModuleInfo loadModuleInfo(File file) throws IOException {
        ModuleInfo.Builder builder = new ModuleInfo.Builder(file);

        if (file.getName().matches("(.+).(jar)")) {
            JarFile jar = new JarFile(file);

            for (ZipEntry entry : Collections.list(jar.entries())) {
                if (entry.getName() != null && entry.getName().equals("__MACOSX")) {
                    continue;
                }

                if (!entry.getName().matches("(.+).(class)")) {
                    continue;
                }

                ClassReader reader = new ClassReader(jar.getInputStream(entry));
                reader.accept(new ModuleClassVisitor(), 0);
            }
        }

//        if (builder.hasMain()) {
//            throw new RuntimeException();
//        }

        return builder.build();
    }
}