package com.example.plugin.base.ext

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode

/**
 * Created by chenyy on 2022/2/16.
 */
fun AbstractInsnNode.methodEnd(): Boolean {
    return ((this.opcode >= Opcodes.IRETURN && this.opcode <= Opcodes.RETURN) || this.opcode == Opcodes.ATHROW)
}