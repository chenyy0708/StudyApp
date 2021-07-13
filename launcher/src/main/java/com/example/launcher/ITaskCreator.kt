package com.example.launcher

/**
 * Created by chenyy on 2021/7/13.
 */

interface ITaskCreator {
    fun createTask(taskName:String):Task
}