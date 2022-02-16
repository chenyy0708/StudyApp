package com.example.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by chenyy on 2021/6/22.
 */

abstract class BaseASMTransPlugin<T : Project> : Plugin<T> {
    override fun apply(project: T) {
        val extension: BaseExtension? = project.extensions.findByType(BaseExtension::class.java)
        extension?.registerTransform(providerTransform())
    }

    abstract fun providerTransform(): BaseASMTransform
}