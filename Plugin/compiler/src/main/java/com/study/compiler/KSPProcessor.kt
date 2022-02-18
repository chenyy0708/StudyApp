package com.study.compiler

import com.example.modulelike.core.BaseAppLike
import com.example.modulelike.core.Const
import com.example.modulelike.core.LikeModel
import com.example.modulelike.core.ModuleProvider
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.study.interfaces.annotation.ModuleLike
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * Created by chenyy on 2022/2/17.
 */

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
class KSPProcessor : AbstractProcessor() {

    var filer: Filer? = null
    var types: Types? = null
    var elements: Elements? = null

    private val mLikeMaps = mutableMapOf<String, LikeModel>()
    private var mHash: String? = null

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        filer = processingEnvironment.filer
        types = processingEnvironment.typeUtils
        elements = processingEnvironment.elementUtils
    }

    override fun getSupportedAnnotationTypes(): Set<String?>? {
        return setOf(ModuleLike::class.java.name)
    }

    /**
     * 生成类
     * public class ModuleInit_xxx {
     *   public companion object {
     *       @JvmStatic
     *       public fun initModules(): Unit {
     *           ModuleProvider.register(com.example.home.HomeAppLike::class.java)
     *       }
     *   }
     *}
     */
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if (annotations?.isNotEmpty() == true && roundEnv?.processingOver() == false) { // 扫描所有的注解标记的类
            println("================KSPProcessor start================")
            val annotatedWith = roundEnv.getElementsAnnotatedWith(ModuleLike::class.java)
            for (element in annotatedWith) {
                val isLike: Boolean = isModuleLike(element)
                if (isLike) {
                    if (mHash == null) {
                        mHash = hash(element.simpleName.toString())
                    }
                    val className = element.toString()
                    mLikeMaps[className] =
                        LikeModel(
                            element.enclosingElement.toString(),
                            element.simpleName.toString()
                        )
                }
            }
        } else if (roundEnv?.processingOver() == true && mLikeMaps.isNotEmpty()) { // 生成ModuleInit_xxx.class
            // 生成initModules方法
            val initBuilder = FunSpec.builder(Const.INIT_METHOD)
            mLikeMaps.forEach { entry ->
                initBuilder.addStatement(
                    "%T.register(%L::class.java)",
                    ModuleProvider::class.java,
                    className(entry.value.pkgName, entry.value.className)
                )
            }
            // 伴生对象
            val companion = TypeSpec.companionObjectBuilder()
                .addFunction(
                    initBuilder
                        .addAnnotation(JvmStatic::class)
                        .build()
                )
                .build()
            // ModuleInit_xxx.KT
            val moduleInit = TypeSpec.classBuilder(Const.MODULE_INIT_NAME + mHash)
                .addType(companion)
                .build()
            val file =
                FileSpec.builder(Const.MODULE_LIKE_GENE_PACKAGE, Const.MODULE_INIT_NAME + mHash)
                    .addType(moduleInit)
                    .build()
            try {
                file.writeTo(filer!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            println("================KSPProcessor end================")
        }
        return false
    }


    /**
     * 非抽象类
     */
    fun isConcreteType(element: Element): Boolean {
        return element is TypeElement && !element.getModifiers().contains(
            Modifier.ABSTRACT
        )
    }

    /**
     * 非抽象子类
     */
    fun isConcreteSubType(element: Element, className: String?): Boolean {
        return isConcreteType(element) && isSubType(element, className)
    }

    fun getClassName(typeMirror: TypeMirror?): String? {
        return typeMirror?.toString() ?: ""
    }

    fun isSubType(type: TypeMirror?, className: String?): Boolean {
        return type != null && types!!.isSubtype(type, typeMirror(className))
    }

    fun isSubType(element: Element?, className: String?): Boolean {
        return element != null && isSubType(element.asType(), className)
    }

    fun isSubType(element: Element?, typeMirror: TypeMirror?): Boolean {
        return element != null && types!!.isSubtype(element.asType(), typeMirror)
    }

    /**
     * 从字符串获取TypeElement对象
     */
    fun typeElement(className: String?): TypeElement {
        return elements!!.getTypeElement(className)
    }

    /**
     * 从字符串获取TypeMirror对象
     */
    fun typeMirror(className: String?): TypeMirror? {
        return typeElement(className).asType()
    }

    fun isModuleLike(element: Element): Boolean {
        return isConcreteSubType(element, BaseAppLike::class.java.name)
    }

    fun hash(str: String): String? {
        return try {
            val md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray())
            BigInteger(1, md.digest()).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            Integer.toHexString(str.hashCode())
        }
    }

    /**
     * 从字符串获取ClassName对象
     */
    fun className(packageName: String, simpleClassName: String): ClassName {
        return ClassName(packageName, simpleClassName)
    }


}