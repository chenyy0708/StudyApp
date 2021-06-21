package com.example.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.commons.Method

/**
 * Created by chenyy on 2021/6/18.
 */

class CustomMethodVisitor(
    mv: MethodVisitor,
    access: Int,
    name: String,
    descriptor: String
) : AdviceAdapter(Opcodes.ASM6, mv, access, name, descriptor) {

    var methodName: String = ""

    init {
        methodName = name
    }

    private var start = 0

    override fun onMethodEnter() {
        super.onMethodEnter()
        println("onMethodEnter  -----$methodName")
        //执行完了怎么办？记录到本地变量中
        invokeStatic(
            Type.getType("Ljava/lang/System;"),
            Method("currentTimeMillis", "()J")
        );
        start = newLocal(Type.LONG_TYPE);
        //创建本地 LONG类型变量
        //记录 方法执行结果给创建的本地变量
        storeLocal(start);
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        println("onMethodEnter  -----$methodName")
        invokeStatic(
            Type.getType("Ljava/lang/System;"),
            Method("currentTimeMillis", "()J")
        );
        val end = newLocal(Type.LONG_TYPE);
        storeLocal(end);
        getStatic(
            Type.getType("Ljava/lang/System;"), "out", Type.getType(
                "Ljava/io" +
                        "/PrintStream;"
            )
        );
        //分配内存 并dup压入栈顶让下面的INVOKESPECIAL 知道执行谁的构造方法创建StringBuilder
        newInstance(Type.getType("Ljava/lang/StringBuilder;"));
        dup();
        invokeConstructor(Type.getType("Ljava/lang/StringBuilder;"), Method("<init>", "()V"));
        visitLdcInsn("execute:");
        invokeVirtual(
            Type.getType("Ljava/lang/StringBuilder;"),
            Method("append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;")
        );
        //减法
        loadLocal(end);
        loadLocal(start);
        math(SUB, Type.LONG_TYPE);
        invokeVirtual(
            Type.getType("Ljava/lang/StringBuilder;"),
            Method("append", "(J)Ljava/lang/StringBuilder;")
        );
        invokeVirtual(
            Type.getType("Ljava/lang/StringBuilder;"),
            Method("toString", "()Ljava/lang/String;")
        );
        invokeVirtual(
            Type.getType("Ljava/io/PrintStream;"),
            Method("println", "(Ljava/lang/String;)V")
        );

    }
}