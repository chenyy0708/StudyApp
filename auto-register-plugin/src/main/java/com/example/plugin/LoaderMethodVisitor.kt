package com.example.plugin

import org.apache.http.util.TextUtils
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Created by chenyy on 2021/6/18.
 */

class LoaderMethodVisitor(
    mv: MethodVisitor,
    access: Int,
    name: String,
    descriptor: String,
    private val moduleAppLikes: MutableMap<String, String>,
    private val loader: String?
) : AdviceAdapter(Opcodes.ASM6, mv, access, name, descriptor) {

    var methodName: String = ""

    init {
        methodName = name
    }

    override fun onMethodEnter() {
        super.onMethodEnter()
        println("onMethodEnter  -----$methodName")
        if (methodName == "onCreate" && !TextUtils.isEmpty(loader)) { // 初始化
            moduleAppLikes.forEach {
                mv.visitVarInsn(Opcodes.ALOAD, 0)
                mv.visitFieldInsn(Opcodes.GETSTATIC, loader, "moduleLikes", "Ljava/util/ArrayList;")
                mv.visitTypeInsn(Opcodes.NEW, it.value)
                mv.visitInsn(Opcodes.DUP)
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, it.value, "<init>", "()V", false)
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false)
                mv.visitInsn(Opcodes.POP)
            }
        }
    }

}