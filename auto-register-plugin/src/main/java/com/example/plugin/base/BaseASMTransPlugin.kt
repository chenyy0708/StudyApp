package com.example.plugin.base

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by chenyy on 2021/6/22.
 */

abstract class BaseASMTransPlugin<T : Project> : Plugin<T> {
    override fun apply(project: T) {
        val appExtension = project.extensions.getByType(
            AppExtension::class.java
        )
        appExtension.registerTransform(providerTransform())
    }

    abstract fun providerTransform(): BaseASMTransform
}