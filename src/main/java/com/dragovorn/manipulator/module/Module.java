package com.dragovorn.manipulator.module;

public @interface Module {

    String name();
    String version() default "1.0.0";
    String author() default "Unknown";
    String[] dependencies() default { };
}