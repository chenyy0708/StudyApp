package com.example.plugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by chenyy on 2021/6/18.
 */

class SearchAppLikeVisitor(
    private val classVisitor: ClassVisitor,
    private val entryName: String,
    private val moduleAppLikes: MutableMap<String, String>,
    private val appLikes: MutableMap<String, String>,
    private val loader: MutableMap<String, String>
) :
    ClassVisitor(Opcodes.ASM6, classVisitor) {

    private var className: String = ""

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name ?: ""
        super.visit(version, access, name, signature, superName, interfaces)
    }

    /**
     * 该方法是当扫描器扫描到类的方法时进行调用
     *
     * @param access     表示方法的修饰符
     * @param name       表示方法名，在 ASM 中 “visitMethod” 方法会处理（构造方法、静态代码块、私有方法、受保护的方法、共有方法、native类型方法）。
     *                   在这些范畴中构造方法的方法名为 “<init>”，静态代码块的方法名为 “<clinit>”。
     * @param desc       表示方法签名，方法签名的格式如下：“(参数列表)返回值类型”
     * @param signature  凡是具有泛型信息的方法，该参数都会有值。并且该值的内容信息基本等于第三个参数的拷贝，只不过不同的是泛型参数被特殊标记出来
     * @param exceptions 用来表示将会抛出的异常，如果方法不会抛出异常，则该参数为空
     * @return
     */
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return classVisitor.visitMethod(access, name, descriptor, signature, exceptions)
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        when (descriptor) {
            MODULE_LIKE -> {
                moduleAppLikes[entryName] = className // module
            }
            APP_LIKE -> {
                appLikes[entryName] = className // application
            }
            LIKE_LOADER -> {
                loader[entryName] = className
            }
        }
        return super.visitAnnotation(descriptor, visible)
    }
}