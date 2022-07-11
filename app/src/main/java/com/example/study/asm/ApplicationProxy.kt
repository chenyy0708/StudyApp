package com.example.study.asm

import android.app.Application
import com.example.launcher.RocketManager
import com.example.modulelike.core.ModuleProvider
import com.example.study.init.task.MatrixTask
import com.example.study.init.task.RouterTask

/**
 * Created by chenyy on 2021/6/28.
 */

object ApplicationProxy {
    fun onCreate(application: Application) {
        ModuleProvider.init()
        val tasks = listOf(RouterTask(), MatrixTask())
        RocketManager.launchRocket(tasks, "AppStartup", false)
    }
}