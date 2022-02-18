package com.example.plugin.modulelike

import com.android.SdkConstants
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.example.modulelike.core.Const
import com.example.plugin.base.BaseTransform
import com.example.plugin.base.DeleteCallBack
import com.example.plugin.base.TransformCallBack
import com.example.plugin.base.utils.ClassUtils
import com.example.plugin.base.utils.Log
import com.google.common.collect.ImmutableSet
import org.apache.commons.compress.utils.IOUtils
import org.objectweb.asm.*
import java.io.*


/**
 * Created by chenyy on 2022/2/16.
 */

class ModuleLikeTransform : Transform() {

    companion object {
        const val TRANSFORM = "ModuleLikeTransform:"
        private const val GENERATE_INIT = "ModuleLike GenerateInit: "
        const val JAR_NAME = "ModuleLike"

        /**
         * Linux/Unix： com/example/modulelike/generated
         * Windows：    com\example\modulelike\generated
         */
        val INIT_SERVICE_DIR = Const.MODULE_LIKE_GENE_PACKAGE.replace('.', File.separatorChar)

        /**
         * com/example/modulelike/generated
         */
        val INIT_SERVICE_PATH = Const.MODULE_LIKE_GENE_PACKAGE.replace('.', '/')
    }

    override fun getName(): String {
        return "ModuleLikeTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf<QualifiedContent.ContentType>().apply {
            addAll(TransformManager.CONTENT_CLASS)
        }
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf<QualifiedContent.ScopeType>().apply {
            addAll(TransformManager.SCOPE_FULL_PROJECT)
        }
    }

    override fun isIncremental(): Boolean {
        return true
    }

//    var initClasses: Set<String> = Collections.newSetFromMap(ConcurrentHashMap())
//    var deleteClasses: Set<String> = Collections.newSetFromMap(ConcurrentHashMap())

    var initClasses = mutableSetOf<String>()
    var deleteClasses = mutableSetOf<String>()

    override fun transform(transformInvocation: TransformInvocation?) {
        val baseTransform = BaseTransform(transformInvocation, object : TransformCallBack {
            override fun process(className: String, classBytes: ByteArray?): ByteArray? {
                val checkClassName = ClassUtils.path2Classname(className)
                if (checkClassName.startsWith(Const.MODULE_LIKE_GENE_PACKAGE) && !checkClassName.contains("$")) {
                    Log.info(
                        TRANSFORM.toString() + "className = %s, checkClassName = %s  className:${className}，checkClassName:${checkClassName}",
                    )
                    initClasses.add(className)
                }
                return null
            }

            override fun finish() {
            }
        })
        baseTransform.setDeleteCallBack(object : DeleteCallBack {
            override fun delete(className: String, bytes: ByteArray) {
                val checkClassName = ClassUtils.path2Classname(className)
                if (checkClassName.startsWith(Const.MODULE_LIKE_GENE_PACKAGE)) {
                    deleteClasses.add(className)
                }
            }
        })
        baseTransform.startTransform()
        // 生成
        val dest: File? = transformInvocation?.outputProvider?.getContentLocation(
            JAR_NAME, TransformManager.CONTENT_CLASS,
            ImmutableSet.of(QualifiedContent.Scope.PROJECT), Format.DIRECTORY
        )
        generateServiceInitClass(dest?.absolutePath ?: "", initClasses, deleteClasses)
    }

    /**
     * 生成格式如下的代码，其中ServiceInit_xxx由注解生成器生成。
     * <pre>
     * package com.sankuai.waimai.router.generated;
     *
     * public class ServiceLoaderInit {
     *
     * public static void init() {
     * ServiceInit_xxx1.init();
     * ServiceInit_xxx2.init();
     * }
     * }
    </pre> *
     */
    private fun generateServiceInitClass(
        directory: String,
        classes: Set<String>,
        deleteClass: Set<String>
    ) {
        if (classes.isEmpty()) {
            Log.info(GENERATE_INIT + "skipped, no service found")
            return
        }
        val dest = File(directory, INIT_SERVICE_PATH + SdkConstants.DOT_CLASS)
        if (!dest.exists()) {
            try {
                Log.info(GENERATE_INIT + "start...")
                val ms = System.currentTimeMillis()
                val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
                val cv: ClassVisitor = object : ClassVisitor(Opcodes.ASM5, writer) {}
                val className = Const.MODULE_LOADER_INIT.replace('.', '/')
                cv.visit(50, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null)
                val mv: MethodVisitor = cv.visitMethod(
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                    Const.INIT_METHOD, "()V", null, null
                )
                mv.visitCode()
                for (clazz in classes) {
                    var input = clazz.replace(".class", "")
                    input = input.replace(".", "/")
                    mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC, input,
                        Const.INIT_METHOD,
                        "()V",
                        false
                    )
                }
                mv.visitMaxs(0, 0)
                mv.visitInsn(Opcodes.RETURN)
                mv.visitEnd()
                cv.visitEnd()
                dest.parentFile.mkdirs()
                FileOutputStream(dest).write(writer.toByteArray())
                Log.info(
                    GENERATE_INIT.toString() + "cost %s ms ${System.currentTimeMillis() - ms}"
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            try {
                modifyClass(dest, classes, deleteClass)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun modifyClass(file: File, items: Set<String?>, deleteItems: Set<String?>) {
        try {
            val inputStream: InputStream = FileInputStream(file)
            val sourceClassBytes: ByteArray = IOUtils.toByteArray(inputStream)
            val modifiedClassBytes = modifyClass(sourceClassBytes, items, deleteItems)
            if (modifiedClassBytes != null) {
                ClassUtils.saveFile(file, modifiedClassBytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun modifyClass(
        srcClass: ByteArray?,
        items: Set<String?>?,
        deleteItems: Set<String?>?
    ): ByteArray? {
        val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val methodFilterCV: ClassVisitor =
            ClassFilterVisitor(
                classWriter,
                items,
                deleteItems
            )
        val cr = ClassReader(srcClass)
        cr.accept(methodFilterCV, ClassReader.SKIP_DEBUG)
        return classWriter.toByteArray()
    }
}