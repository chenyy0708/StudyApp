package com.example.plugin

import com.android.build.gradle.BaseExtension
import com.example.plugin.modulelike.ModuleLikeTransform
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * Created by chenyy on 2021/6/18.
 */
class StudyTransformPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension: BaseExtension? = project.extensions.findByType(BaseExtension::class.java)
        extension?.registerTransform(ModuleLikeTransform())
        project.afterEvaluate { p: Project? ->
        }
    }
}