package com.dragovorn.manipulator.command;

public class InvalidCommandTypeException extends RuntimeException {

    public InvalidCommandTypeException() {
        super("You cannot register a custom Command child");
    }
}