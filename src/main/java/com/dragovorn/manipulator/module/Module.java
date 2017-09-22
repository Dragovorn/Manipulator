package com.dragovorn.manipulator.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Module {

    String name();
    String version() default "1.0.0";
    String author() default "Unknown";
    String[] dependencies() default { };
}