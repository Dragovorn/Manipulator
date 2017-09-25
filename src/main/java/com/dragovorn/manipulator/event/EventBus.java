package com.dragovorn.manipulator.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private Map<Class<? extends Event>, List<Method>> listeners;

    public EventBus() {
        this.listeners = new HashMap<>();
    }

    public void fireEvent(Event event) {
        this.listeners.computeIfPresent(event.getClass(), ((event1, methods) -> {
            methods.forEach((method -> {
                try {
                    method.invoke(event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }));

            return null;
        }));
    }

    public void registerListener(Class<? extends Event> event, Method listener) {
        List<Method> listeners = this.listeners.computeIfAbsent(event, (method) -> new ArrayList<>());
        this.listeners.put(event, listeners);

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
}