package com.example.launcher.rocket4j;

import java.util.List;

/**
 * 任务运行快照信息
 */
public class TaskSnapshot {
    private String mTaskName;
    private TaskRunStatus mTaskRunStatus;
    private List<String> mStacktrace;

    public TaskSnapshot(String taskName, TaskRunStatus taskRunStatus, List<String> stacktrace) {
        mTaskName = taskName;
        mTaskRunStatus = taskRunStatus;
        mStacktrace = stacktrace;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public TaskRunStatus getTaskRunStatus() {
        return mTaskRunStatus;
    }

    public void setTaskRunStatus(TaskRunStatus taskRunStatus) {
        mTaskRunStatus = taskRunStatus;
    }

    public List<String> getStacktrace() {
        return mStacktrace;
    }

    public void setStacktrace(List<String> stacktrace) {
        mStacktrace = stacktrace;
    }

    @Override
    public String toString() {
        return "TaskSnapshot{" +
                "mTaskName='" + mTaskName + '\'' +
                ", mTaskRunStatus=" + mTaskRunStatus +
                ", mStacktrace=" + mStacktrace +
                '}';
    }
}