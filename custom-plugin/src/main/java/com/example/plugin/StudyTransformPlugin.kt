package com.example.plugin

import com.example.plugin.base.BaseASMTransPlugin
import com.example.plugin.base.BaseASMTransform
import org.gradle.api.Project

/**
 * Created by chenyy on 2021/6/18.
 */
class StudyTransformPlugin : BaseASMTransPlugin<Project>() {
    override fun providerTransform(): BaseASMTransform = MethodTransform()
}