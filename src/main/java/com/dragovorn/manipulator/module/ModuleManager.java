package com.dragovorn.manipulator.module;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.command.Command;
import com.dragovorn.manipulator.command.executor.CommandExecutor;
import com.dragovorn.manipulator.command.console.CommandConsole;
import com.dragovorn.manipulator.command.game.CommandGame;
import com.dragovorn.manipulator.event.Event;
import com.dragovorn.manipulator.event.module.FinishEnablingModulesEvent;
import com.dragovorn.manipulator.module.asm.ModuleClassVisitor;
import com.dragovorn.manipulator.util.FileUtil;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
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
        Manipulator.getInstance().getLogger().info("Injecting module discoverer into modules folder...");

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

        Manipulator.getInstance().getLogger().info("Finished discovering modules!");
        Manipulator.getInstance().getLogger().info("Loading modules...");

        Map<ModuleInfo, Boolean> pluginStatus = new HashMap<>();

        for (Map.Entry<String, ModuleInfo> entry : this.load.entrySet()) {
            if (!loadPlugin(pluginStatus, new Stack<>(), entry.getValue())) {
                Manipulator.getInstance().getLogger().log(Level.INFO, "Failed to enable {0}", entry.getKey());
            }
        }

        Manipulator.getInstance().getLogger().info("Finished loading modules!");

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
                ModuleLoader loader = new ModuleLoader(new URL[] {info.getFile().toURI().toURL()});

                Class<?> main = loader.loadClass(info.getMain());
                ManipulatorModule clazz = (ManipulatorModule) main.getDeclaredConstructor().newInstance();
                clazz.init(info, loader);

                this.modules.put(info.getName(), clazz);
                Manipulator.getInstance().getLogger().log(Level.INFO, "Loading {0} v{1} by {2}...", new Object[] { info.getName(), info.getVersion(), info.getAuthor() });

                clazz.onLoad();

                Manipulator.getInstance().getLogger().log(Level.INFO, "Loaded {0} v{1} by {2}!", new Object[] { info.getName(), info.getVersion(), info.getAuthor() });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return false;
            }
        }

        pluginStatus.put(info, status);

        return status;
    }

    public void enableModules() {
        Manipulator.getInstance().getLogger().info("Enabling modules...");

        for (ManipulatorModule plugin : this.modules.values()) {
            try {
                Manipulator.getInstance().getLogger().log(Level.INFO, "Enabling {0} v{1} by {2}...", new Object[] { plugin.getInfo().getName(), plugin.getInfo().getVersion(), plugin.getInfo().getAuthor() });

                plugin.onEnable();

                if (plugin.getInfo().hasCommands()) {
                    List<Command> commands = new ArrayList<>();

                    if (!plugin.getInfo().getConsoleCommands().isEmpty()) {
                        plugin.getLogger().log(Level.INFO, "Registering {0} console command(s)...", new Object[] { plugin.getInfo().getConsoleCommands().size() });

                        plugin.getInfo().getConsoleCommands().forEach((name, path) -> {
                            try {
                                commands.add(new CommandConsole((CommandExecutor) plugin.getLoader().loadClass(path).getDeclaredConstructor().newInstance(), name));
                            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    if (!plugin.getInfo().getGameCommands().isEmpty()) {
                        plugin.getLogger().log(Level.INFO, "Registering {0} game command(s)...", new Object[] { plugin.getInfo().getGameCommands().size() });

                        plugin.getInfo().getGameCommands().forEach((name, path) -> {
                            try {
                                commands.add(new CommandGame((CommandExecutor) plugin.getLoader().loadClass(path).getDeclaredConstructor().newInstance(), name));
                            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    commands.forEach(Manipulator.getInstance()::registerCommand);
                }

                if (plugin.getInfo().hasListeners()) {
                    plugin.getLogger().log(Level.INFO, "Registering {0} listener(s)...", new Object[] { plugin.getInfo().getNumListeners() });

                    plugin.getInfo().getListeners().forEach((event, listeners) -> listeners.forEach((clazz, methods) -> {
                        try {
                            Class<? extends Event> eventClass = (Class<? extends Event>) plugin.getLoader().loadClass(event);
                            Class listener = plugin.getLoader().loadClass(clazz);

                            methods.forEach(method -> {
                                try {
                                    Manipulator.getInstance().getEventBus().registerListener(eventClass, listener.getDeclaredMethod(method, eventClass), plugin);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                            });

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }));
                }

                Manipulator.getInstance().getLogger().log(Level.INFO, "Enabled {0} v{1} by {2}!", new Object[] { plugin.getInfo().getName(), plugin.getInfo().getVersion(), plugin.getInfo().getName() });
            } catch (Throwable throwable) {
                Manipulator.getInstance().getLogger().log(Level.WARNING, "Encountered exception while enabling module: " + plugin.getInfo().getName(), throwable);
            }
        }

        Manipulator.getInstance().getLogger().info("Finished enabling modules!");

        Manipulator.getInstance().getEventBus().fireEvent(new FinishEnablingModulesEvent());
    }

    public void disableModules() {
        Manipulator.getInstance().getLogger().info("Disabling modules...");

        for (ManipulatorModule plugin : this.modules.values()) {
            try {
                Manipulator.getInstance().getLogger().log(Level.INFO, "Disabling {0} v{1} by {2}...", new Object[] { plugin.getInfo().getName(), plugin.getInfo().getVersion(), plugin.getInfo().getAuthor() });

                plugin.onDisable();

                Manipulator.getInstance().getLogger().log(Level.INFO, "Disabled {0} v{1} by {2}!", new Object[] { plugin.getInfo().getName(), plugin.getInfo().getVersion(), plugin.getInfo().getAuthor() });
            } catch (Throwable throwable) {
                Manipulator.getInstance().getLogger().log(Level.WARNING, "Encountered exception while disabling module: " + plugin.getInfo().getName(), throwable);
            }
        }

        Manipulator.getInstance().getLogger().info("Finished disabling modules!");
    }

    private ModuleInfo loadModuleInfo(File file) throws IOException {
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
                reader.accept(new ModuleClassVisitor(builder), 0);
            }
        }

        if (!builder.hasMain()) {
            throw new RuntimeException();
        }

        return builder.build();
    }
}