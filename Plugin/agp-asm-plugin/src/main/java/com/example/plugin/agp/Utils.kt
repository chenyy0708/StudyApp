package com.example.plugin.agp

import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode

/**
 * Created by chenyy on 2022/5/5.
 */

val ClassNode.simpleClassName: String
    get() = name.substringAfterLast('/')

fun MethodInsnNode.insertArgument(argumentType: Class<*>) {
    val type = Type.getMethodType(desc)
    val argumentTypes = type.argumentTypes
    val returnType = type.returnType
    val newArgumentTypes = arrayOfNulls<Type>(argumentTypes.size + 1)
    System.arraycopy(argumentTypes, 0, newArgumentTypes, 0, argumentTypes.size - 1 + 1)
    newArgumentTypes[newArgumentTypes.size - 1] = Type.getType(argumentType)
    desc = Type.getMethodDescriptor(returnType, *newArgumentTypes)
}