package com.dragovorn.manipulator.module.asm;

import com.dragovorn.manipulator.command.CommandExecutor;
import com.dragovorn.manipulator.command.console.ConsoleCommand;
import com.dragovorn.manipulator.command.game.GameCommand;
import com.dragovorn.manipulator.module.ManipulatorModule;
import com.dragovorn.manipulator.module.Module;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class ModuleClassVisitor extends ClassVisitor {

    private Type type;

    private String name;

    private boolean implementsExecutor;
    private boolean extendsMain;

    enum Type {
        MAIN,
        CONSOLE,
        GAME,
        EVENT,
        NONE
    }

    public ModuleClassVisitor() {
        super(Opcodes.ASM6);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name;
        this.extendsMain = superName.equals(ManipulatorModule.class.getCanonicalName().replaceAll("\\.", "/"));
        this.implementsExecutor = Arrays.stream(interfaces).anyMatch(str -> str.equals(CommandExecutor.class.getName().replaceAll("\\.", "/")));
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("L" + Module.class.getCanonicalName().replaceAll("\\.", "/") + ";")) {
            this.type = Type.MAIN;
        } else if (desc.equals("L" + ConsoleCommand.class.getCanonicalName().replaceAll("\\.", "/") + ";")) {
            this.type = Type.CONSOLE;
        } else if (desc.equals("L" + GameCommand.class.getCanonicalName().replaceAll("\\.", "/") + ";")) {
            this.type = Type.GAME;
        }

        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitEnd() {
        System.out.println(this.name + "(type=" + this.type + ", CmdExecutor=" + this.implementsExecutor + ", Module=" + this.extendsMain + ")");
        super.visitEnd();
    }
}