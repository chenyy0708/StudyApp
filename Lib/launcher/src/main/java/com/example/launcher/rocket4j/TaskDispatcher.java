package com.example.launcher.rocket4j;

import com.example.launcher.rocket4j.util.ThrowableUtil;

public class TaskDispatcher extends Thread {
    private TaskQueue mTaskQueue;
    private Rocket mRocket;
    private volatile boolean mQuit = false;


    TaskDispatcher(Rocket rocket, TaskQueue taskQueue) {
        this.mRocket = rocket;
        this.mTaskQueue = taskQueue;
    }

    @Override
    public void run() {
        setPriority(Thread.NORM_PRIORITY);
        while (true) {
            try {
                this.mRocket.getLogger().d("[Rocket分发器][%s] Taking下一个任务...", getName());
                Task nextRunningTask = mTaskQueue.takeNextRunnableTask();
                if (nextRunningTask.isNeedImmediately()) {
                    this.mRocket.getLogger().d("[Rocket分发器][%s] 立刻执行任务：%s", getName(), nextRunningTask.getTaskName());
                } else {
                    this.mRocket.getLogger().d("[Rocket分发器][%s] 等待执行任务：%s 当前rocket状态isPause ：%s", getName(), nextRunningTask.getTaskName(), mRocket.mRocketLock.isPaused());
                    mRocket.mRocketLock.await();
                }

                this.mRocket.getLogger().d("[Rocket分发器][%s] 任务 [%s] 进入执行状态.", getName(), nextRunningTask.getTaskName());
                TaskCompleteEmitterImpl taskCompleteEmitter = new TaskCompleteEmitterImpl();
                nextRunningTask.runWithNotify(taskCompleteEmitter);
                taskCompleteEmitter.waiting();
                mTaskQueue.completeTaskAndNotify(nextRunningTask);
                this.mRocket.getLogger().d("[Rocket分发器][%s] 任务 [%s] 进入完成状态", getName(), nextRunningTask.getTaskName());
                mTaskQueue.judgeToAddNextRunnableTasks(nextRunningTask);
                mTaskQueue.judgeToStopQueueAndNotify();
            } catch (InterruptedException e) {
                if (mQuit) {
                    this.mRocket.getLogger().d("[Rocket分发器][%s] 退出.", getName());
                    return;
                }
                this.mRocket.getLogger().d("[Rocket分发器][%s] 不退出，但是发生了阻断异常:%s", getName(), ThrowableUtil.getThrowableString(e));
            }

        }
    }

    void quit() {
        mQuit = true;
        interrupt();
    }
}