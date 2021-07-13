package com.example.launcher

import androidx.annotation.IntDef

/**
 * Created by chenyy on 2021/7/12.
 */
@Retention()
@IntDef(TaskState.IDLE, TaskState.START, TaskState.RUNNING, TaskState.FINISHED)
annotation class TaskState {
    companion object {
        const val IDLE = 0
        const val START = 1
        const val RUNNING = 2
        const val FINISHED = 3
    }
}