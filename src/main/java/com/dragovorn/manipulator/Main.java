package com.dragovorn.manipulator;

public class Main {

    public static void main(String... args) {
        new Manipulator(!(args.length == 1 && args[0].equalsIgnoreCase("nogui")));
    }
}