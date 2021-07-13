package com.example.launcher

import android.util.SparseArray

/**
 * Created by chenyy on 2021/7/13.
 */

class TaskRuntimeInfo( val task: Task) {

     val stateTime = SparseArray<Long>()
    var isBlockTask = false
    var threadName: String? = null

    fun setStateTime(@TaskState state: Int, time: Long) {
        stateTime.put(state, time)
    }

    fun isSameTask(task: Task?): Boolean {
        return task != null && this.task === task
    }

    override fun toString(): String {
        return "TaskRuntimeInfo(task=$task, stateTime=$stateTime, isBlockTask=$isBlockTask, threadName=$threadName)"
    }
}