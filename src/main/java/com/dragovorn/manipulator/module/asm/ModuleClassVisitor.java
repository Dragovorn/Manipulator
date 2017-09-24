package com.dragovorn.manipulator.module.asm;

import com.dragovorn.manipulator.command.CommandExecutor;
import com.dragovorn.manipulator.command.console.ConsoleCommand;
import com.dragovorn.manipulator.command.game.GameCommand;
import com.dragovorn.manipulator.module.ManipulatorModule;
import com.dragovorn.manipulator.module.Module;
import com.dragovorn.manipulator.util.StringUtil;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleClassVisitor extends ClassVisitor {

    private List<ClassType> types;

    private String name;

    private boolean implementsExecutor;
    private boolean extendsMain;

    public enum ClassType {
        MAIN,
        CONSOLE,
        GAME,
        LISTENER,
        NONE
    }

    public ModuleClassVisitor() {
        super(Opcodes.ASM6);

        this.types = new ArrayList<>();
        this.types.add(ClassType.NONE);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name;
        this.extendsMain = superName.equals(StringUtil.formatClassPath(ManipulatorModule.class));
        this.implementsExecutor = Arrays.stream(interfaces).anyMatch(str -> str.equals(StringUtil.formatClassPath(CommandExecutor.class)));
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("L" + StringUtil.formatClassPath(Module.class) + ";")) {
            add(ClassType.MAIN, this.types);
        } else if (desc.equals("L" + StringUtil.formatClassPath(ConsoleCommand.class) + ";")) {
            add(ClassType.CONSOLE, this.types);
        } else if (desc.equals("L" + StringUtil.formatClassPath(GameCommand.class) + ";")) {
            add(ClassType.GAME, this.types);
        } else {
            System.out.println(desc);
        }

        return super.visitAnnotation(desc, visible);
    }

    @Override
    public MethodVisitor visitMethod(int var1, String name, String desc, String var4, String[] var5) {
        Type[] types = Type.getArgumentTypes(desc);

        return new ModuleMethodVisitor(name, types, this.types);
    }

    @Override
    public void visitEnd() {
        System.out.println(this.name + "(types=" + this.types + ", CmdExecutor=" + this.implementsExecutor + ", Module=" + this.extendsMain + ")");
        super.visitEnd();
    }

    static void add(ClassType type, List<ClassType> list) {
        if (list.get(0) == ClassType.NONE) {
            list.set(0, type);
        } else if (!list.contains(type)) {
            list.add(type);
        }
    }
}