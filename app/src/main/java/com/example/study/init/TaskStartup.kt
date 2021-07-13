package com.example.study.init

import android.util.Log
import com.example.launcher.ITaskCreator
import com.example.launcher.Project
import com.example.launcher.Task
import com.example.launcher.TaskFlowManager

/**
 * Created by chenyy on 2021/7/13.
 */

object TaskStartup {
    const val TASK_BLOCK_1 = "block_task_1"
    const val TASK_BLOCK_2 = "block_task_2"
    const val TASK_BLOCK_3 = "block_task_3"

    const val TASK_ASYNC_1 = "async_block_1"
    const val TASK_ASYNC_2 = "async_block_2"
    const val TASK_ASYNC_3 = "async_block_3"

    fun start() {
        Log.d("TaskStartup", "start")
        val project = Project.Builder("TaskStartup", createTaskCreator())
            .add(TASK_BLOCK_1)
            .add(TASK_BLOCK_2)
            .add(TASK_BLOCK_3)
            .add(TASK_ASYNC_1).dependOn(TASK_BLOCK_1)
            .add(TASK_ASYNC_2).dependOn(TASK_BLOCK_2)
            .add(TASK_ASYNC_3).dependOn(TASK_BLOCK_3)
            .build()

        TaskFlowManager
            .addBlockTask(TASK_BLOCK_1)
            .addBlockTask(TASK_BLOCK_2)
            .addBlockTask(TASK_BLOCK_3)
            .start(project)
    }

    private fun createTaskCreator(): ITaskCreator {
        return object : ITaskCreator {
            override fun createTask(taskName: String): Task {
                when (taskName) {
                    TASK_ASYNC_1 -> return createTask(taskName, true)
                    TASK_ASYNC_2 -> return createTask(taskName, true)
                    TASK_ASYNC_3 -> return createTask(taskName, true)

                    TASK_BLOCK_1 -> return createTask(taskName, false)
                    TASK_BLOCK_2 -> return createTask(taskName, false)
                    TASK_BLOCK_3 -> return createTask(taskName, false)
                }
                return createTask("default", false)
            }
        }
    }

    private fun createTask(taskName: String, isAsync: Boolean): Task {
        return object : Task(taskName, isAsync) {
            override fun run(taskName: String) {
                Thread.sleep(if (isAsync) 2000 else 1000)
                Log.d("TaskStartup", "task $taskName , $isAsync , finished")
            }

        }
    }
}