package com.example.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by chenyy on 2021/6/18.
 */
class StudyTransformPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("===========  doing  ============")
        val appExtension = project.extensions.getByType(
            AppExtension::class.java
        )
        appExtension.registerTransform(MethodTransform())
    }
}