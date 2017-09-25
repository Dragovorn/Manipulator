package com.dragovorn.manipulator.module.asm;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.event.Event;
import com.dragovorn.manipulator.event.Listener;
import com.dragovorn.manipulator.util.StringUtil;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

class ModuleMethodVisitor extends MethodVisitor {

    private String name;

    private Type[] types;

    private List<ModuleClassVisitor.ClassType> classTypes;

    private ModuleClassVisitor visitor;

    ModuleMethodVisitor(String name, Type[] types, List<ModuleClassVisitor.ClassType> classTypes, ModuleClassVisitor visitor) {
        super(Opcodes.ASM6);

        this.name = name;
        this.types = types;
        this.classTypes = classTypes;
        this.visitor = visitor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean var2) {
        if (desc.equals("L" + StringUtil.formatClassPath(Listener.class) + ";")) {
            try {
                if (this.types.length == 1) {
                    if (Class.forName(this.types[0].getClassName()).isAssignableFrom(Event.class)) {
                        this.visitor.builder.addListener(this.types[0].toString().substring(1, this.types[0].toString().length() - 1).replaceAll("/", "."), this.visitor.name.replaceAll("/", "."), this.name);
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