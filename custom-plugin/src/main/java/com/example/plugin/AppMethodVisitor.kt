package com.example.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Created by chenyy on 2021/6/18.
 */

class AppMethodVisitor(
    mv: MethodVisitor,
    access: Int,
    name: String,
    descriptor: String,
    private val moduleAppLikes: MutableMap<String, String>,
    private val appLikes: MutableMap<String, String>
) : AdviceAdapter(Opcodes.ASM6, mv, access, name, descriptor) {

    var methodName: String = ""

    init {
        methodName = name
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        println("onMethodExit  -----$methodName")
        if (methodName == "onCreate") {
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitFieldInsn(Opcodes.GETSTATIC, "com/example/study/asm/ServiceLoader", "INSTANCE", "Lcom/example/study/asm/ServiceLoader;")
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/example/study/asm/ServiceLoader", "onCreate", "()V", false)
            mv.visitInsn(Opcodes.POP)
        }
    }
}