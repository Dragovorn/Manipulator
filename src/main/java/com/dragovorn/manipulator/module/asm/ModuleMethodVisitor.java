package com.dragovorn.manipulator.module.asm;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.event.Event;
import com.dragovorn.manipulator.event.Listener;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

class ModuleMethodVisitor extends MethodVisitor {

    private String name;

    private Type[] types;

    private List<ModuleClassVisitor.ClassType> classTypes;

    ModuleMethodVisitor(String name, Type[] types, List<ModuleClassVisitor.ClassType> classTypes) {
        super(Opcodes.ASM6);

        this.name = name;
        this.types = types;
        this.classTypes = classTypes;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean var2) {
        if (desc.equals("L" + Listener.class.getCanonicalName().replaceAll("\\.", "/") + ";")) {
            try {
                if (this.types.length == 1) {
                    if (Class.forName(this.types[0].getClassName()).isAssignableFrom(Event.class)) {
                        // TODO add listener to list to be registered
                        ModuleClassVisitor.add(ModuleClassVisitor.ClassType.LISTENER, this.classTypes);
                    } else {
                        Manipulator.getInstance().getLogger().warning("Ignoring " + this.name + ", malformed listener method.");
                    }
                } else {
                    Manipulator.getInstance().getLogger().warning("Ignoring " + this.name + ", malformed listener method.");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return super.visitAnnotation(desc, var2);
    }
}