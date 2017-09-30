package com.dragovorn.manipulator.module;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ModuleLoader extends URLClassLoader {

    private static final Set<ModuleLoader> allLoaders = new CopyOnWriteArraySet<>();

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public ModuleLoader(URL[] urls) {
        super(urls);
        allLoaders.add(this);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve, true);
    }

    private Class<?> loadClass(String name, boolean resolve, boolean checkOther) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException exception) { /* Do Nothing */ }

        if (checkOther) {
            for (ModuleLoader loader : allLoaders) {
                if (loader != this) {
                    try {
                        return loader.loadClass(name, resolve, false);
                    } catch (ClassNotFoundException exception) { /* Do Nothing */ }
                }
            }
        }

        throw new ClassNotFoundException(name);
    }
}