package com.example.launcher

/**
 * Created by chenyy on 2021/7/13.
 */

class Project private constructor(taskName: String) : Task(taskName) {

    lateinit var endTask: Task
    lateinit var startTask: Task

    override fun start() {
        startTask.start()
    }

    override fun run(taskName: String) {
        // 不需要处理
    }

    override fun dependOn(dependTask: Task) {
        startTask.dependOn(dependTask)
    }

    override fun behind(behindTask: Task) {
        endTask.behind(behindTask)
    }

    override fun removeBehind(behindTask: Task) {
        endTask.removeBehind(behindTask)
    }

    override fun removeDependence(dependTask: Task) {
        startTask.removeDependence(dependTask)
    }


    class Builder(projectName: String, taskCreator: ITaskCreator) {
        private val mTaskFactory = TaskFactory(taskCreator)

        private val mStartTask: Task = CriticalTask(projectName + "_start")
        private val mEndTask: Task = CriticalTask(projectName + "_end")
        private val mProject: Project = Project(projectName)
        private var mPriority = 0

        // 本次添加的start，是否依赖start节点
        private var mCurrentTaskShouldDependOnStartTask = true
        private var mCurrentAddTask: Task? = null

        fun add(taskName: String): Builder {
            val task = mTaskFactory.getTask(taskName)
            if (task.priority > mPriority) {
                mPriority = task.priority
            }
            return add(task)
        }

        fun add(task: Task): Builder {
            if (mCurrentTaskShouldDependOnStartTask && mCurrentAddTask != null) {
                mStartTask.behind(mCurrentAddTask!!)
            }
            mCurrentAddTask = task
            mCurrentTaskShouldDependOnStartTask = true
            mCurrentAddTask?.behind(mEndTask)
            return this
        }

        fun dependOn(taskName: String): Builder {
            return dependOn(mTaskFactory.getTask(taskName))
        }

        private fun dependOn(task: Task): Builder {
            mCurrentAddTask?.let { task.behind(it) }
            mEndTask.removeDependence(task)
            mCurrentTaskShouldDependOnStartTask = false
            return this
        }

        fun build(): Project {
            if (mCurrentAddTask == null) {
                mStartTask.behind(mEndTask)
            } else {
                if (mCurrentTaskShouldDependOnStartTask) {
                    mStartTask.behind(mCurrentAddTask!!)
                }
            }
            mStartTask.priority = mPriority
            mEndTask.priority = mPriority
            mProject.startTask = mStartTask
            mProject.endTask = mEndTask

            return mProject
        }
    }

     class CriticalTask internal constructor(taskName: String) : Task(taskName) {
        override fun run(taskName: String) {
        }
    }

    private class TaskFactory(private val taskCreator: ITaskCreator) {
        private val mCacheTasks = mutableMapOf<String, Task>()

        fun getTask(taskName: String): Task {
            var task = mCacheTasks[taskName]
            if (task != null) return task
            task = taskCreator.createTask(taskName)
            mCacheTasks[taskName] = task
            return task
        }
    }
}