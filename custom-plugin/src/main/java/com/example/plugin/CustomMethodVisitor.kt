package com.example.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

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

    override fun onMethodEnter() {
        super.onMethodEnter()
        println("onMethodEnter  -----$methodName")
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        println("onMethodEnter  -----$methodName")
    }
}