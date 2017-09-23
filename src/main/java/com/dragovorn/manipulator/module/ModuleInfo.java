package com.dragovorn.manipulator.module;

import java.io.File;

public class ModuleInfo {

    private File file;

    private String name;
    private String main;
    private String author;
    private String version;
    private String[] dependencies;

    static class Builder {

        private File file;

        private String name;
        private String main;
        private String author;
        private String version;
        private String[] dependencies;

        public Builder(File file) {
            this.file = file;
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

        public boolean hasMain() {
            return !this.main.isEmpty();
        }

        public ModuleInfo build() {
            return new ModuleInfo(this.file, this.name, this.main, this.author, this.version, this.dependencies);
        }
    }

    private ModuleInfo(File file, String name, String main, String author, String version, String[] dependencies) {
        this.file = file;
        this.name = name;
        this.main = main;
        this.author = author;
        this.version = version;
        this.dependencies = dependencies;
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
}