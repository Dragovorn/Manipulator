package com.dragovorn.manipulator;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException {
        new Manipulator(!(args.length == 1 && args[0].equalsIgnoreCase("nogui")));
    }
}