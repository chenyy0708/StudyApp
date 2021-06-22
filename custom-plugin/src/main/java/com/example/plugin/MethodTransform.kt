package com.example.plugin

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import com.example.plugin.base.BaseASMTransform
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by chenyy on 2021/6/18.
 */
class MethodTransform : BaseASMTransform() {
    override fun getName(): String {
        return "StudyMethodTransform"
    }

    override fun processJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        val dest = outputProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        //将修改过的字节码copy到dest,就可以实现编译期间干预字节码的目的
        try {
            FileUtils.copyFile(jarInput.file, dest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun processDirectoryInput(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    ) {
        if (directoryInput.file.isDirectory) {
            directoryInput.file.walk()
                .filter { it.isFile }
                .filter { it.name.endsWith("MainActivity.class") }
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
        try {
            FileUtils.copyDirectory(directoryInput.file, dest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private var time = 0L

    override fun transformStart(transformInvocation: TransformInvocation) {
        time = System.currentTimeMillis()
        println("--------------- MethodTransform --START------------- ")
    }

    override fun transformEnd(transformInvocation: TransformInvocation) {
        println("--------------- MethodTransform --END------------- ")
        println("--------------- 耗时 ${System.currentTimeMillis()-time}ms------------- ")
    }
}