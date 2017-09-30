package com.dragovorn.manipulator.event;

import com.dragovorn.manipulator.module.ManipulatorModule;
import com.dragovorn.manipulator.util.AtomicObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventBus {

    private Map<Class<? extends Event>, Map<ManipulatorModule, Map<Method, Object>>> listeners;

    public EventBus() {
        this.listeners = new HashMap<>();
    }

    public void fireEvent(Event event) {
        this.listeners.computeIfPresent(event.getClass(), (eventClass, modules) -> {
            modules.forEach((module, methods) -> methods.forEach((method, invocationTarget) -> {
                try {
                    method.invoke(invocationTarget, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }));

            return modules;
        });
    }

    public void registerListener(Class<? extends Event> event, Method method, ManipulatorModule module) {
        Map<ManipulatorModule, Map<Method, Object>> modules = this.listeners.computeIfAbsent(event, (event1) -> new HashMap<>());
        Map<Method, Object> methods = modules.computeIfAbsent(module, (module1) -> new HashMap<>());

        modules.put(module, methods);
        this.listeners.put(event, modules);

        if (!methods.containsKey(method)) {
            if (method.getDeclaringClass().equals(module.getClass())) {
                methods.put(method, module);
            } else {
                AtomicObject object = new AtomicObject();
                AtomicBoolean hasObject = new AtomicBoolean(false);

                methods.forEach((key, value) -> {
                    if (value.getClass().equals(method.getDeclaringClass())) {
                        hasObject.set(true);
                        object.set(value);
                    }
                });

                if (hasObject.get()) {
                    methods.put(method, object.get());
                }
            }
        }
    }
}