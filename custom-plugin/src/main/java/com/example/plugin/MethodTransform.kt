package com.example.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by chenyy on 2021/6/18.
 */
class MethodTransform : Transform() {
    override fun getName(): String {
        return "StudyMethodTransform"
    }

    /**
     * 需要处理的数据类型,这里表示class文件
     */
    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 作用范围,子项目和本地项目  排除外部依赖 节省编译时间
     */
    override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否支持增量编译
     */
    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(transformInvocation: TransformInvocation) {
        println("--------------- MethodTransform --START------------- ")
        val inputs = transformInvocation.inputs
        //TransformOutputProvider管理输出路径,如果消费型输入为空,则outputProvider也为空
        val outputProvider = transformInvocation.outputProvider
        //遍历inputs
        for (input in inputs) {
            //遍历directoryInputs
            for (directoryInput in input.directoryInputs) {
                //处理directoryInputs
                processDirectoryInput(directoryInput, outputProvider)
            }
            //遍历jarInputs
            for (jarInput in input.jarInputs) {
                //处理jarInputs
                processJarInput(jarInput, outputProvider)
            }
        }
        println("'--------------- MethodTransform--END --------------- '")
    }

    fun processJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        val dest = outputProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        //将修改过的字节码copy到dest,就可以实现编译期间干预字节码的目的
        println("拷贝文件  -----$dest")
        try {
            FileUtils.copyFile(jarInput.file, dest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun processDirectoryInput(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    ) {
        if (directoryInput.file.isDirectory) {
            directoryInput.file.walk()
                .filter { it.isFile }
                .filter { it.name.endsWith(".class") }
                .filter { it.name.contains("MainActivity") }
                .forEach { file ->
                    val classReader = ClassReader(file.readBytes())
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val cv = CustomVisitor(classWriter)
                    classReader.accept(cv, EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    val fos = FileOutputStream(
                        file.parentFile.absolutePath + File.separator + file.name
                    )
                    fos.write(code)
                    fos.close()
                    println("processDirectoryInput:${file}")
                }
        }
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        //将修改过的字节码copy到dest,就可以实现编译期间干预字节码的目的
        println("拷贝文件夹  -----$dest")
        try {
            FileUtils.copyDirectory(directoryInput.file, dest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}