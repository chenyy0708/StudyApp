package com.example.plugin

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import com.example.plugin.base.BaseASMTransform
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.compress.utils.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * Created by chenyy on 2021/6/18.
 */
class MethodTransform : BaseASMTransform() {
    override fun getName(): String {
        return "StudyMethodTransform"
    }

    private val moduleAppLikes = mutableMapOf<String, String>()
    private val appLikes = mutableMapOf<String, String>()
    private val loader = mutableMapOf<String, String>()

    override fun processJarInput(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider,
        isFirst: Boolean
    ) {
        // 排除非JAR
        if (!jarInput.file.absolutePath.endsWith(".jar")) {
            copyJar(jarInput, outputProvider)
            return
        }
        //重名名输出文件,因为可能同名,会覆盖
        var jarName = jarInput.name
        val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
        jarName = jarName.substring(0, jarName.length - 4)
        val jarFile = JarFile(jarInput.file)
        // 排除其他远程依赖
        if (!jarFile.name.contains("runtime_library_classes_jar")) {
            copyJar(jarInput, outputProvider)
            return
        }
        val entries = jarFile.entries()
        if (isFirst) { // 扫描代码
            // 第一遍遍历，得到注解标记的App
            while (entries.hasMoreElements()) {
                val jarEntry: JarEntry = entries.nextElement()
                val entryName = jarEntry.name
                val inputStream = jarFile.getInputStream(jarEntry)
                if (checkClassFile(entryName)) { // 插桩
                    val classReader = ClassReader(IOUtils.toByteArray(inputStream))
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val cv =
                        SearchAppLikeVisitor(
                            classWriter,
                            entryName,
                            moduleAppLikes,
                            appLikes,
                            loader
                        )
                    classReader.accept(cv, EXPAND_FRAMES)
                }
            }
            val dest = outputProvider.getContentLocation(
                jarInput.file.absolutePath,
                jarInput.contentTypes,
                jarInput.scopes,
                Format.JAR
            )
            try {
                FileUtils.copyFile(jarInput.file, dest)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else { // 插入代码
            val tmpFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))
            // 第二遍循环，注入代码
            while (entries.hasMoreElements()) {
                val jarEntry: JarEntry = entries.nextElement()
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = jarFile.getInputStream(jarEntry)
                jarOutputStream.putNextEntry(zipEntry)
                if (checkClassFile(entryName)) { // 插桩
                    val classReader = ClassReader(IOUtils.toByteArray(inputStream))
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val cv =
                        SearchAppLikeVisitor(
                            classWriter,
                            entryName,
                            moduleAppLikes,
                            appLikes,
                            loader
                        )
                    classReader.accept(cv, EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            //将修改过的字节码copy到dest,就可以实现编译期间干预字节码的目的
            val dest = outputProvider.getContentLocation(
                jarName + md5Name,
                jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            try {
                FileUtils.copyFile(jarInput.file, dest)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun checkClassFile(entryName: String): Boolean {
        return (entryName.endsWith(".class") && !entryName.startsWith("R\$")
                && "R.class" != entryName && "BuildConfig.class" != entryName)
    }

    private fun copyJar(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        //将修改过的字节码copy到dest,就可以实现编译期间干预字节码的目的
        val dest = outputProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        try {
            FileUtils.copyFile(jarInput.file, dest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun processDirectoryInput(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider,
        isFirst: Boolean
    ) {
        if (directoryInput.file.isDirectory) {
            directoryInput.file.walk()
                .filter { it.isFile }
                .filter { checkClassFile(it.name) }
                .forEach { file ->
                    if (isFirst) {
                        val classReader = ClassReader(file.readBytes())
                        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                        val cv =
                            SearchAppLikeVisitor(
                                classWriter,
                                file.absolutePath,
                                moduleAppLikes,
                                appLikes,
                                loader
                            )
                        classReader.accept(cv, EXPAND_FRAMES)
                    } else {
                        val classReader = ClassReader(file.readBytes())
                        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                        val cv = AutoRegisterVisitor(
                            classWriter,
                            file.absolutePath,
                            moduleAppLikes,
                            appLikes,
                            loader
                        )
                        classReader.accept(cv, EXPAND_FRAMES)
                        val code = classWriter.toByteArray()
                        val fos = FileOutputStream(
                            file.parentFile.absolutePath + File.separator + file.name
                        )
                        fos.write(code)
                        fos.close()
                    }
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
        println("--------------- 耗时 ${System.currentTimeMillis() - time}ms------------- ")
    }

    override fun isTransformTwo(): Boolean = true
}