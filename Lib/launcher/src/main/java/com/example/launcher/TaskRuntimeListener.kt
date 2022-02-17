package com.example.launcher

import android.util.Log
import com.example.launcher.TaskRuntime.getTaskRuntimeInfo

/**
 * Created by chenyy on 2021/7/12.
 */

class TaskRuntimeListener : TaskListener {
    override fun onStart(task: Task) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, task.taskName + START)
        }
    }

    override fun onRunning(task: Task) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, task.taskName + RUNNING)
        }
    }

    override fun onFinished(task: Task) {
        if (BuildConfig.DEBUG) {
            logTaskRuntimeInfo(task)
        }

    }

    private fun logTaskRuntimeInfo(task: Task) {
        val taskRuntimeInfo = getTaskRuntimeInfo(taskName = task.taskName) ?: return
        val startTime = taskRuntimeInfo.stateTime[TaskState.START]
        val runningTime = taskRuntimeInfo.stateTime[TaskState.RUNNING]
        val finishedTime = taskRuntimeInfo.stateTime[TaskState.FINISHED]

        val builder = StringBuilder()
        builder.append(WRAPPER)
        builder.append(TAG)
        builder.append(WRAPPER)

        builder.append(WRAPPER)
        builder.append(HALF_LINE)
        builder.append(if (task is Project) "Project" else "task${task.taskName}" + FINISHED_TIME)
        builder.append(HALF_LINE)

        addTaskInfoLIneInfo(builder, DEPENDENCIES, getTaskDependenciesInfo(task))
        addTaskInfoLIneInfo(builder, THREAD_NAME, taskRuntimeInfo.isBlockTask.toString())
        addTaskInfoLIneInfo(builder, THREAD_NAME, taskRuntimeInfo.threadName.toString())
        addTaskInfoLIneInfo(builder, START_TIME, startTime.toString() + "ms")
        addTaskInfoLIneInfo(builder, WAITING_TIME, (runningTime - startTime).toString() + "ms")
        addTaskInfoLIneInfo(builder, TASK_CONSUME, (finishedTime - runningTime).toString() + "ms")
        addTaskInfoLIneInfo(builder, FINISHED_TIME, finishedTime.toString() + "ms")

        builder.append(HALF_LINE)
        builder.append(HALF_LINE)
        builder.append(WRAPPER)
        builder.append(WRAPPER)

        if (BuildConfig.DEBUG) {
            Log.d(TAG, builder.toString())
        }
    }

    private fun addTaskInfoLIneInfo(
        builder: StringBuilder,
        key: String,
        value: String
    ) {
        builder.append("$key:$value")
    }

    private fun getTaskDependenciesInfo(task: Task): String {
        val builder = StringBuilder()
        task.dependTaskNames.forEach {
            builder.append("$it ")
        }
        return builder.toString()
    }

    companion object {
        const val TAG: String = "TaskFlow"
        const val START = "-- onStart --"
        const val RUNNING = "-- onRunning --"
        const val FINISHED = "-- onFinished --"

        const val DEPENDENCIES = "依赖任务"
        const val THREAD_NAME = "线程名称"
        const val START_TIME = "开始执行时刻"
        const val WAITING_TIME = "等待执行耗时"
        const val TASK_CONSUME = "任务执行耗时"
        const val IS_BLOC_TASK = "是否是阻塞任务"
        const val FINISHED_TIME = "任务结束时刻"
        const val WRAPPER = "\n"
        const val HALF_LINE = "========================="
    }
}