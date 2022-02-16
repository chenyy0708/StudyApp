package com.example.plugin.base

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by chenyy on 2022/2/16.
 */

interface PluginProvider {

    fun getPlugin(): Class<out Plugin<Project>>


    fun dependOn(): List<String>
}