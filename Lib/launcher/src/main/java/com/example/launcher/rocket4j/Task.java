package com.example.launcher.rocket4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

/**
 * 需要执行的任务
 */
public class Task {
    public static final int DEFAULT_PRIORITY = 0;

    private Rocket mRocket;
    private String mTaskName;
    private int mOriginPriority;
    private int mPriority;
    private Set<String> mOriginDependsOn;
    private Set<String> mDependsOn;
    protected Thread mRunningThread;
    private volatile boolean isNeedImmediately;
    private volatile TaskRunStatus mTaskStatus;
    private final Object mMutexForPriority = new Object();
    private final Object mMutexForDependsOn = new Object();

    public Task() {
    }

    public Task(String taskName, Set<String> dependsOn) {
        this(taskName, DEFAULT_PRIORITY, dependsOn, TaskRunStatus.WAITING);
    }

    public Task(String taskName, int priority, Set<String> dependsOn) {
        this(taskName, priority, dependsOn, TaskRunStatus.WAITING);
    }

    private Task(String taskName, int priority, Set<String> dependsOn, TaskRunStatus taskStatus) {
        mTaskName = taskName;
        mOriginPriority = priority;
        mPriority = priority;
        mOriginDependsOn = new HashSet<>(dependsOn);
        mDependsOn = new HashSet<>(dependsOn);
        mTaskStatus = taskStatus;
    }

    /**
     * 执行任务，结束的时候需要自己调用结束
     *
     * @param emitter
     */
    public void runWithNotify(TaskCompleteEmitter emitter) {
        mRunningThread = Thread.currentThread();
        run();
        emitter.onComplete();
        mRunningThread = null;
    }

    /**
     * 执行任务
     */
    public void run() {
    }

    /**
     * 复制新的task
     *
     * @return
     */
    public Task copy() {
        return new Task(this.mTaskName, this.mPriority, this.mDependsOn, this.mTaskStatus);
    }

    public String getTaskName() {
        return mTaskName;
    }

    public int getPriority() {
        return mPriority;
    }

    public Set<String> getOriginDependsOn() {
        return mOriginDependsOn;
    }

    public int getOriginPriority() {
        return mOriginPriority;
    }

    void raisePriority() {
        synchronized (mMutexForPriority) {
            mPriority = mPriority + 1;
        }
    }

    void raisePriority(int gap) {
        synchronized (mMutexForPriority) {
            mPriority = mPriority + gap;
        }
    }

    public Set<String> getDependsOn() {
        return mDependsOn;
    }

    boolean removeDepends(String taskName) {
        synchronized (mMutexForDependsOn) {
            return mDependsOn.remove(taskName);
        }
    }

    public TaskRunStatus getTaskStatus() {
        return mTaskStatus;
    }

    void setTaskStatus(TaskRunStatus taskStatus) {
        mTaskStatus = taskStatus;
    }

    void setRocket(Rocket rocket) {
        this.mRocket = rocket;
    }

    public Rocket getRocket() {
        return mRocket;
    }

    public Thread getRunningThread() {
        return mRunningThread;
    }

    public void setNeedImmediately(boolean needImmediately) {
        isNeedImmediately = needImmediately;
    }

    public boolean isNeedImmediately() {
        return isNeedImmediately;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return mOriginPriority == task.mOriginPriority &&
                Objects.equals(mRocket, task.mRocket) &&
                Objects.equals(mTaskName, task.mTaskName) &&
                Objects.equals(mOriginDependsOn, task.mOriginDependsOn) &&
                Objects.equals(mRunningThread, task.mRunningThread) &&
                mTaskStatus == task.mTaskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mRocket, mTaskName, mOriginPriority, mPriority, mOriginDependsOn, mDependsOn, mRunningThread, mTaskStatus);
    }

    String toDetailString() {
        return "Task{" +
                "mRocket=" + mRocket +
                ", mTaskName='" + mTaskName + '\'' +
                ", mOriginPriority=" + mOriginPriority +
                ", mPriority=" + mPriority +
                ", mOriginDependsOn=" + mOriginDependsOn +
                ", mDependsOn=" + mDependsOn +
                ", mRunningThread=" + mRunningThread +
                ", mTaskStatus=" + mTaskStatus +
                '}';
    }

    @Override
    public String toString() {
        return mTaskName + ":" + mPriority;
    }


    public interface TaskListener {
        void onTaskStart(Task task);

        void onTaskEnd(Task task);
    }

    public static class TaskSimpleListener implements TaskListener {

        @Override
        public void onTaskStart(Task task) {

        }

        @Override
        public void onTaskEnd(Task task) {

        }
    }

    private final Vector<TaskListener> mTaskListeners = new Vector<>();

    /**
     * 注册任务的监听
     *
     * @param l
     */
    void register(TaskListener l) {
        mTaskListeners.add(l);
    }

    /**
     * 反注册任务监听
     *
     * @param l
     */
    void unregister(TaskListener l) {
        mTaskListeners.remove(l);
    }

    void notifyStart() {
        Object[] copyed = mTaskListeners.toArray();
        for (Object taskListener : copyed) {
            ((TaskListener) taskListener).onTaskStart(this);
        }
    }

    void notifyEnd() {
        Object[] copyed = mTaskListeners.toArray();
        for (Object taskListener : copyed) {
            ((TaskListener) taskListener).onTaskEnd(this);
        }
    }
}