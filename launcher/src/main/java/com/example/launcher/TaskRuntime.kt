package com.example.launcher

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executors

/**
 * Created by chenyy on 2021/7/13.
 */

internal object TaskRuntime {
    val mMainHandler = Handler(Looper.getMainLooper())

    val blockTaskNames = mutableListOf<String>()

    val waitingTasks = mutableListOf<Task>()

    val taskRuntimeInfo = mutableMapOf<String, TaskRuntimeInfo>()

    @JvmStatic
    fun addBlockTask(taskName: String?) {
        if (!taskName.isNullOrBlank()) {
            blockTaskNames.add(taskName)
        }

    }

    @JvmStatic
    fun addBlockTasks(vararg taskNames: String) {
        if (taskNames.isNotEmpty()) {
            taskNames.forEach { addBlockTask(it) }
        }
    }

    @JvmStatic
    fun removeBlockTasks(taskName: String?) {
        if (!taskName.isNullOrBlank()) {
            blockTaskNames.remove(taskName)
        }
    }

    @JvmStatic
    fun hasWaitingTasks(): Boolean {
        return waitingTasks.iterator().hasNext()
    }

    @JvmStatic
    fun hasBlockTasks(): Boolean {
        return blockTaskNames.iterator().hasNext()
    }

    @JvmStatic
    fun setThreadName(task: Task, threadName: String?) {
        val taskRuntimeInfo = getTaskRuntimeInfo(task.taskName)
        taskRuntimeInfo?.threadName = threadName
    }

    @JvmStatic
    fun setStateInfo(task: Task) {
        val taskRuntimeInfo = getTaskRuntimeInfo(task.taskName)
        taskRuntimeInfo?.setStateTime(task.state, System.currentTimeMillis())
    }

    @JvmStatic
    fun getTaskRuntimeInfo(taskName: String): TaskRuntimeInfo? {
        return taskRuntimeInfo[taskName]
    }


    @JvmStatic
    private fun executeTask(task: Task) {
        if (task.isAsyncTask) {
            Executors.newCachedThreadPool().execute(task)
        } else {
            if (task.delayMills > 0 && !hasBlockBehindTask(task)) {
                mMainHandler.postDelayed(task, task.delayMills)
                return
            }

            if (!hasBlockTasks()) {
                task.run()
            } else {
                addWaitingTask(task)
            }
        }
    }

    private fun addWaitingTask(task: Task) {
        if (!waitingTasks.contains(task)) {
            waitingTasks.add(task)
        }
    }

    private fun hasBlockBehindTask(task: Task): Boolean {
        if (task is Project.CriticalTask) {
            return false
        }
        val behindTasks = task.behindTasks
        behindTasks.forEach {
            val behindTaskInfo = getTaskRuntimeInfo(it.taskName)
            if (behindTaskInfo != null && behindTaskInfo.isBlockTask) return true
            // 递归遍历
            return hasBlockBehindTask(it)
        }
        return false
    }

    fun traversalDependencyTreeAndInit(task: Task) {
        val traversalVisitor = linkedSetOf<Task>()
        traversalVisitor.add(task)
        innerTraversalDependencyTreeAndInit(task, traversalVisitor)

        val iterator = blockTaskNames.iterator()
        while (iterator.hasNext()) {
            val taskName = iterator.next()
            if (!taskRuntimeInfo.containsKey(taskName)) {
                throw RuntimeException("block task ${task.taskName} not in dependency tree.")
            } else {
                val task = getTaskRuntimeInfo(taskName)?.task
                traversalDependencyPriority(task)
            }
        }
    }

    private fun traversalDependencyPriority(task: Task?) {
        if (task == null) return
    }

    private fun innerTraversalDependencyTreeAndInit(
        task: Task,
        traversalVisitor: LinkedHashSet<Task>
    ) {
        var taskRuntimeInfo = getTaskRuntimeInfo(task.taskName)
        if (taskRuntimeInfo == null) {
            taskRuntimeInfo = TaskRuntimeInfo(task)
            if (blockTaskNames.contains(task.taskName)) {
                taskRuntimeInfo.isBlockTask = true
            }
            this.taskRuntimeInfo[task.taskName] = taskRuntimeInfo
        } else {
            if (!taskRuntimeInfo.isSameTask(task)) {
                throw RuntimeException("not allow to contain the same id ${task.taskName}")
            }
        }

        task.behindTasks.forEach {
            if (!traversalVisitor.contains(it)) {
                traversalVisitor.add(it)
            } else {
                throw RuntimeException("not allow loopback dependency , task name = ${task.taskName}")
            }
            if (BuildConfig.DEBUG && it.behindTasks.isEmpty()) {
                val iterator = traversalVisitor.iterator()
                val builder = StringBuilder()
                while (iterator.hasNext()) {
                    builder.append(iterator.next().taskName)
                    builder.append(" ---> ")
                }
                Log.d(TaskRuntimeListener.TAG, builder.toString())
            }
            innerTraversalDependencyTreeAndInit(it, traversalVisitor)
            traversalVisitor.remove(it)
        }
    }

    fun runWaitingTasks() {
        if (hasWaitingTasks()) {
            if (waitingTasks.size > 1) {
                waitingTasks.sortWith(kotlin.Comparator { task1, task2 ->
                    return@Comparator when {
                        task1.priority > task2.priority -> -1
                        task1.priority < task2.priority -> 1
                        else -> 0
                    }
                })
            }

            if (hasBlockTasks()) {
                val head = waitingTasks.removeAt(0)
                head.run()
            } else {
                waitingTasks.forEach {
                    mMainHandler.postDelayed(it, it.delayMills)
                }
                waitingTasks.clear()
            }
        }
    }
}