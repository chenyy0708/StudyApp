package com.example.study.init

import android.app.Application
import com.example.launcher.rocket4j.Task
import com.example.study.MyApp

/**
 * Created by chenyy on 2022/7/11.
 */

open class BaseTask : Task {

    constructor(taskName: String, dependsOn: Set<String>) : super(taskName, dependsOn)
    constructor(taskName: String, priority: Int, dependsOn: Set<String>) : super(
        taskName,
        priority,
        dependsOn
    )

    open override fun run() {
        super.run()
    }

    fun getAppApplication(): Application? {
        return MyApp.instance
    }
}