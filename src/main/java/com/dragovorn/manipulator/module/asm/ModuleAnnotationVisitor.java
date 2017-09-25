package com.dragovorn.manipulator.module.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

public class ModuleAnnotationVisitor extends AnnotationVisitor {

    private ModuleClassVisitor visitor;

    private ModuleClassVisitor.ClassType type;

    public ModuleAnnotationVisitor(ModuleClassVisitor.ClassType type, ModuleClassVisitor visitor) {
        super(Opcodes.ASM6);

        this.type = type;
        this.visitor = visitor;
    }

    @Override
    public void visit(String name, Object value) {
        if (this.type == ModuleClassVisitor.ClassType.CONSOLE) {
            this.visitor.console = (String) value;
        } else if (this.type == ModuleClassVisitor.ClassType.GAME) {
            this.visitor.game = (String) value;
        } else if (this.type == ModuleClassVisitor.ClassType.MAIN) {
            switch (name) {
                case "name":
                    this.visitor.builder.setName((String) value);
                    break;
                case "version":
                    this.visitor.builder.setVersion((String) value);
                    break;
                case "author":
                    this.visitor.builder.setAuthor((String) value);
                    break;
                case "dependencies":
                    this.visitor.builder.setDependencies((String[]) value);
                    break;
            }
        }

        super.visit(name, value);
    }
}