package com.dragovorn.manipulator.util;

public class StringUtil {

    public static String formatClassPath(Class<?> clazz) {
        return clazz.getCanonicalName().replaceAll("\\.", "/");
    }
}