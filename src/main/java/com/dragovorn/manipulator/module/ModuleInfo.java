package com.dragovorn.manipulator.module;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleInfo {

    private File file;

    private Map<String, List<String>> listeners;
    private Map<String, String> consoleCommands;
    private Map<String, String> gameCommands;

    private String name;
    private String main;
    private String author;
    private String version;
    private String[] dependencies;

    public static class Builder {

        private File file;

        private Map<String, List<String>> listeners;
        private Map<String, String> consoleCommands;
        private Map<String, String> gameCommands;

        private String name;
        private String main;
        private String author = "Unknown";
        private String version = "1.0.0";
        private String[] dependencies = new String[0];

        Builder(File file) {
            this.file = file;
            this.consoleCommands = new HashMap<>();
            this.gameCommands = new HashMap<>();
            this.listeners = new HashMap<>();
        }

        public Builder addListener(String path, String methodName) {
            List<String> methods = this.listeners.computeIfAbsent(path, (str) -> new ArrayList<>());

            methods.add(methodName);

            this.listeners.put(path, methods);

            return this;
        }

        public Builder putConsole(String name, String path) {
            this.consoleCommands.put(name, path);

            return this;
        }

        public Builder putGame(String name, String path) {
            this.gameCommands.put(name, path);

            return this;
        }

        public Builder setName(String name) {
            this.name = name;

            return this;
        }

        public Builder setMain(String main) {
            this.main = main;

            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;

            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;

            return this;
        }

        public Builder setDependencies(String[] dependencies) {
            this.dependencies = dependencies;

            return this;
        }

        boolean hasMain() {
            return !this.main.isEmpty();
        }

        ModuleInfo build() {
            return new ModuleInfo(this.file, this.name, this.main, this.author, this.version, this.dependencies, this.consoleCommands, this.gameCommands, this.listeners);
        }
    }

    private ModuleInfo(File file, String name, String main, String author, String version, String[] dependencies, Map<String, String> consoleCommands, Map<String, String> gameCommands, Map<String, List<String>> listeners) {
        this.file = file;
        this.name = name;
        this.main = main;
        this.author = author;
        this.version = version;
        this.dependencies = dependencies;
        this.consoleCommands = consoleCommands;
        this.gameCommands = gameCommands;
        this.listeners = listeners;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getMain() {
        return this.main;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getVersion() {
        return this.version;
    }

    public String[] getDependencies() {
        return this.dependencies;
    }

    public Map<String, String> getConsoleCommands() {
        return this.consoleCommands;
    }

    public Map<String, String> getGameCommands() {
        return this.gameCommands;
    }

    public Map<String, List<String>> getListeners() {
        return this.listeners;
    }

    public int getNumListeners() {
        AtomicInteger num = new AtomicInteger(0);

        this.listeners.forEach((path, list) -> num.addAndGet(list.size()));

        return num.get();
    }

    public boolean hasCommands() {
        return !this.gameCommands.isEmpty() || !this.consoleCommands.isEmpty();
    }

    public boolean hasListeners() {
        return !this.listeners.isEmpty();
    }
}