package com.dragovorn.manipulator.util;

import java.io.File;
import java.io.InputStream;

public class FileUtil {

    public static final File log = new File("logs");
    public static final File modules = new File("modules");

    public static void createDirectories() {
        if (!log.exists()) {
            log.mkdirs();
        }

        if (!modules.exists()) {
            modules.mkdirs();
        }
    }

    public static InputStream getResource(String path) {
        return FileUtil.class.getResourceAsStream((path.startsWith("/") ? "" : "/") + path);
    }
}