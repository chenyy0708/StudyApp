package com.example.launcher

/**
 * Created by chenyy on 2021/7/13.
 */

object TaskFlowManager {
    fun addBlockTask(taskName: String): TaskFlowManager {
        TaskRuntime.addBlockTask(taskName)
        return this
    }

    fun addBlockTasks(vararg taskNames: String): TaskFlowManager {
        TaskRuntime.addBlockTasks(*taskNames)
        return this
    }

    fun start(task: Task) {
        val startTask = if (task is Project) task.startTask else task
        TaskRuntime.traversalDependencyTreeAndInit(startTask)
        startTask.start()

        while (TaskRuntime.hasBlockTasks()) {
            try {
                Thread.sleep(10)
            } catch (ex: Exception) {
            }
            while (TaskRuntime.hasBlockTasks()) {
                TaskRuntime.runWaitingTasks()
            }
        }
    }
}