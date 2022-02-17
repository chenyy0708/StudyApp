package com.example.launcher

/**
 * Created by chenyy on 2021/7/12.
 */

interface TaskListener {
    fun onStart(task: Task)
    fun onRunning(task: Task)
    fun onFinished(task: Task)
}