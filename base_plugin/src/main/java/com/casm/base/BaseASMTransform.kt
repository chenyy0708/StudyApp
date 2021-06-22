package com.casm.base

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager

/**
 * Created by chenyy on 2021/6/22.
 */

abstract class BaseASMTransform : Transform() {

    /**
     * 是否支持增量编译
     */
    override fun isIncremental(): Boolean {
        return true
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

    override fun transform(transformInvocation: TransformInvocation) {
        transformStart(transformInvocation)
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
        transformEnd(transformInvocation)
    }

    abstract fun transformStart(transformInvocation: TransformInvocation)

    abstract fun transformEnd(transformInvocation: TransformInvocation)

    abstract fun processJarInput(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider
    )

    abstract fun processDirectoryInput(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    )
}