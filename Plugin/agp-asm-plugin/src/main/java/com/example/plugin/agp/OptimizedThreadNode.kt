package com.example.plugin.agp

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

/**
 * Created by chenyy on 2022/5/5.
 */

class OptimizedThreadNode(val nextClassVisitor: ClassVisitor) : ClassNode(Opcodes.ASM5) {

    companion object {

        private const val threadClass = "java/lang/Thread"

        private const val threadFactoryClass = "java/util/concurrent/ThreadFactory"

        private const val threadFactoryNewThreadMethodDesc =
            "newThread(Ljava/lang/Runnable;)Ljava/lang/Thread;"

        val config = OptimizedThreadConfig()
    }


    override fun visitEnd() {
        super.visitEnd()
        val taskList = mutableListOf<() -> Unit>()
        this.methods?.forEach { methodNode ->
            val instructionIterator = methodNode.instructions?.iterator()
            if (instructionIterator != null) {
                while (instructionIterator.hasNext()) {
                    val instruction = instructionIterator.next()
                    when (instruction.opcode) {
                        Opcodes.NEW -> {
                            val typeInsnNode = instruction as? TypeInsnNode
                            if (typeInsnNode?.desc == threadClass) {
                                //如果是在 ThreadFactory 内初始化线程，则不处理
                                if (!isThreadFactoryMethod(methodNode)) {
                                    println("=========isThreadFactoryMethod:${methodNode.name}")
                                    taskList.add {
                                        transformNew(
                                            this,
                                            methodNode,
                                            instruction
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        taskList.forEach {
            it.invoke()
        }
        accept(nextClassVisitor)
    }


    private fun transformNew(
        classNode: ClassNode,
        methodNode: MethodNode,
        typeInsnNode: TypeInsnNode
    ) {
        val instructions = methodNode.instructions
        val typeInsnNodeIndex = instructions.indexOf(typeInsnNode)
        //从 typeInsnNode 指令开始遍历，找到调用 Thread 构造函数的指令，然后对其进行替换
        for (index in typeInsnNodeIndex + 1 until instructions.size()) {
            val node = instructions[index]
            if (node is MethodInsnNode && node.isThreadInitMethodInsn()) {
                //将 Thread 替换为 OptimizedThread
                typeInsnNode.desc = config.formatOptimizedThreadClass
                node.owner = config.formatOptimizedThreadClass
                //为调用 Thread 构造函数的指令多插入一个 String 类型的方法入参参数声明
                node.insertArgument(String::class.java)
                //将 ClassName 作为构造参数传给 OptimizedThread
                instructions.insertBefore(node, LdcInsnNode(classNode.simpleClassName))
                break
            }
        }
    }

    private fun MethodInsnNode.isThreadInitMethodInsn(): Boolean {
        return this.owner == threadClass && this.name == "<init>"
    }

    private fun ClassNode.isThreadFactoryMethod(methodNode: MethodNode): Boolean {
        return this.interfaces?.contains(threadFactoryClass) == true
                && methodNode.name == threadFactoryNewThreadMethodDesc
    }
}