package com.example.launcher

import android.os.Trace
import java.util.*

/**
 * Created by chenyy on 2021/7/12.
 */

abstract class Task constructor(
    val taskName: String,
    val isAsyncTask: Boolean = true,
    val delayMills: Long = 0,
    var priority: Int = 0
) : Runnable, Comparable<Task> {
    // 执行时间
    var executeTime: Long = 0
        protected set

    @TaskState
    var state: Int = TaskState.IDLE
        protected set

    val dependTasks = mutableListOf<Task>()
    val dependTaskNames = mutableListOf<String>()
    val behindTasks = mutableListOf<Task>()
    val behindTaskNames = mutableListOf<String>()
    private val taskListeners = mutableListOf<TaskListener>()
    private var taskRuntimeListener: TaskRuntimeListener? = TaskRuntimeListener()

    fun addTask(taskListener: TaskListener) {
        if (!taskListeners.contains(taskListener)) {
            taskListeners.add(taskListener)
        }
    }

    fun removeTask(taskListener: TaskListener) {
        if (taskListeners.contains(taskListener)) {
            taskListeners.remove(taskListener)
        }
    }

    open fun start() {
        if (state != TaskState.IDLE)
            throw RuntimeException("重复执行 Task $taskName")
        toStart()
        executeTime = System.currentTimeMillis()
        executeTask(this)
    }

    private fun executeTask(task: Task) {

    }

    private fun toStart() {
        state = TaskState.START
        TaskRuntime.setStateInfo(this)
        taskListeners.forEach { it.onStart(this) }
        taskRuntimeListener?.onStart(this)
    }

    private fun toRunning() {
        state = TaskState.RUNNING
        TaskRuntime.setStateInfo(this)
        TaskRuntime.setThreadName(this, Thread.currentThread().name)
        taskListeners.forEach { it.onRunning(this) }
        taskRuntimeListener?.onRunning(this)
    }

    private fun toFinished() {
        state = TaskState.FINISHED
        TaskRuntime.setStateInfo(this)
        TaskRuntime.removeBlockTasks(taskName)
        taskListeners.forEach { it.onFinished(this) }
        taskRuntimeListener?.onFinished(this)
    }

    override fun run() {
        // 改变任务状态
        Trace.beginSection(taskName)
        toRunning()
        run(taskName)
        toFinished()
        // 通知后置任务执行
        notifyBehindTasks()
        recycle()
        Trace.endSection()
    }

    private fun notifyBehindTasks() {
        if (behindTasks.isEmpty()) return
        if (behindTasks.size > 1) {
            behindTasks.sortWith(Comparator { task1, task2 ->
                return@Comparator when {
                    task1.priority > task2.priority -> -1
                    task1.priority < task2.priority -> 1
                    else -> 0
                }
            })
        }

        behindTasks.forEach {
            it.dependTaskFinished(this)
        }
    }

    private fun dependTaskFinished(dependTask: Task) {
        if (dependTasks.isEmpty()) return
        dependTasks.remove(dependTask)
        if (dependTasks.isEmpty()) {
            start()
        }
    }

    private fun recycle() {
        dependTasks.clear()
        behindTasks.clear()
        taskListeners.clear()
        taskRuntimeListener = null
    }


    abstract fun run(taskName: String)

    override fun compareTo(other: Task): Int {
        return when {
            this.priority > other.priority -> -1
            this.priority < other.priority -> 1
            else -> 0
        }
    }

    open fun dependOn(dependTask: Task) {
        if (dependTask == this) return
        var task = dependTask
        if (dependTask is Project) {
            task = dependTask.endTask
        }
        dependTasks.add(task)
        dependTaskNames.add(task.taskName)
        if (!dependTask.behindTasks.contains(this)) {
            dependTask.behindTasks.add(this)
        }
    }

    open fun removeDependence(dependTask: Task) {
        if (dependTask == this) return

        var task = dependTask
        if (dependTask is Project) {
            task = dependTask.endTask
        }
        dependTasks.remove(task)
        dependTaskNames.remove(task.taskName)

        if (dependTask.behindTasks.contains(this)) {
            dependTask.behindTasks.remove(this)
        }
    }

    open fun behind(behindTask: Task) {
        if (behindTask == this) return

        var task = behindTask
        if (behindTask is Project) {
            task = behindTask.startTask
        }

        behindTasks.add(task)
        behindTaskNames.add(task.taskName)

        behindTask.dependOn(this)
    }

    open fun removeBehind(behindTask: Task) {
        if (behindTask == this) return

        var task = behindTask
        if (behindTask is Project) {
            task = behindTask.startTask
        }

        behindTasks.remove(task)
        behindTaskNames.add(task.taskName)


        behindTask.removeBehind(this)
    }
}