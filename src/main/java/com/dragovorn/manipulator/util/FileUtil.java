package com.dragovorn.manipulator.util;

import java.io.InputStream;

public class FileUtil {

    public static InputStream getResource(String path) {
        return FileUtil.class.getResourceAsStream((path.startsWith("/") ? "" : "/") + path);
    }
}