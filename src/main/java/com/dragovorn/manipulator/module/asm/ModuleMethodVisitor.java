package com.dragovorn.manipulator.module.asm;

import com.dragovorn.manipulator.event.Event;
import com.dragovorn.manipulator.event.Listener;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class ModuleMethodVisitor extends MethodVisitor {

    private Type[] types;

    ModuleMethodVisitor(Type[] types) {
        super(Opcodes.ASM6);

        this.types = types;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean var2) {
        if (desc.equals("L" + Listener.class.getCanonicalName().replaceAll("\\.", "/") + ";")) {
            try {
                if (this.types.length == 1) {
                    if (Class.forName(this.types[0].getClassName()).isAssignableFrom(Event.class)) {
                        System.out.println("    Found good listener!");
                    } else {
                        System.out.println("    " + this.types[0] + " doesn't extend Event!");
                    }
                } else {
                    System.out.println("    A listener should have one parameter!");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return super.visitAnnotation(desc, var2);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}