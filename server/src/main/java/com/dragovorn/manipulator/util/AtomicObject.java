package com.dragovorn.manipulator.util;

public class AtomicObject {

    private Object object;

    public AtomicObject() {
        this(null);
    }

    public AtomicObject(Object object) {
        this.object = object;
    }

    public synchronized void set(Object object) {
        this.object = object;
    }

    public synchronized Object get() {
        return this.object;
    }
}