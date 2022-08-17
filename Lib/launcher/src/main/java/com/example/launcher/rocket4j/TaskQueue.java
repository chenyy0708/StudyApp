package com.example.launcher.rocket4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;

import com.example.launcher.rocket4j.util.ThreadUtil;

/**
 * 任务队列
 */
public class TaskQueue {
    private Rocket mRocket;
    //可运行的任务队列
    private PriorityBlockingSupportUpdateQueue<Task> mRunnableTaskQueue;
    //原始的所有任务
    private Map<String, Task> mAllTasks;
    private List<Task> mTasksByRunOrder;
    private final TaskDispatcher[] mDispatchers;
    private final Object mMutexForWating2Runnable = new Object();
    private final Object mMutexForRunning2Complete = new Object();
    private volatile boolean mIsTaskQueueEnd;

    TaskQueue(Rocket rocket, Rocket.Config config) {
        mRocket = rocket;
        mIsTaskQueueEnd = false;
        mAllTasks = new HashMap<>();
        for (Task task : config.getTasks()) {
            task.setRocket(rocket);
            mAllTasks.put(task.getTaskName(), task);
        }
        mRunnableTaskQueue = new PriorityBlockingSupportUpdateQueue<>(mAllTasks.size(), new TaskPriorityComparator());
        //找出所有入度为0的task，加入mRunnableTaskQueue
        initRunnableTasks();
        mTasksByRunOrder = Collections.synchronizedList(new ArrayList<Task>());
        mDispatchers = new TaskDispatcher[config.getThreadPoolSize()];

        mRocket.getLogger().d("[Rocket队列] 初始化完成\n当前可执行队列：%s\n所有任务：%s", mRunnableTaskQueue, config.getTasks());
    }

    private void initRunnableTasks() {
        for (Map.Entry<String, Task> entry : mAllTasks.entrySet()) {
            if (entry.getValue().getTaskStatus() == TaskRunStatus.WAITING
                    && entry.getValue().getDependsOn().isEmpty()) {
                entry.getValue().setTaskStatus(TaskRunStatus.RUNNABLE);
                mRunnableTaskQueue.put(entry.getValue());
            }
        }
    }


    void start() {
        notifyTaskQueueStart();
        synchronized (mDispatchers) {
            mRocket.getLogger().d("[Rocket队列] 开始，开启所有分发器 >>>>>>>>>>>>>>>>>>>>>>>");
            for (int i = 0; i < mDispatchers.length; i++) {
                TaskDispatcher dispatcher = new TaskDispatcher(mRocket, this);
                mDispatchers[i] = dispatcher;
                dispatcher.setName("Rocket-" + i);
                dispatcher.start();
            }
        }
    }

    private void stop() {
        synchronized (mDispatchers) {
            for (TaskDispatcher mDispatcher : mDispatchers) {
                if (mDispatcher != null) {
                    mDispatcher.quit();
                }
            }
            mRocket.getLogger().d("[Rocket队列] 全部结束，停止所有分发器 >>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    /**
     * 从任务列表中找到所有其他可执行的任务
     * WAITING -> RUNNABLE
     */
    void judgeToAddNextRunnableTasks(Task task) {
        synchronized (mMutexForWating2Runnable) {
            for (Map.Entry<String, Task> entry : mAllTasks.entrySet()) {
                Task t = entry.getValue();
                if (t.getTaskStatus() == TaskRunStatus.WAITING
                        && t.getDependsOn().contains(task.getTaskName())) {
                    t.removeDepends(task.getTaskName());
                    if (t.getDependsOn().isEmpty()) {
                        t.setTaskStatus(TaskRunStatus.RUNNABLE);
                        mRunnableTaskQueue.put(t);
                        mRocket.getLogger().d("[Rocket队列] 任务 [%s] 减少依赖[%s]，进入可执行状态", t.getTaskName(), task.getTaskName());
                    } else {
                        mRocket.getLogger().d("[Rocket队列] 任务 [%s] 减少依赖[%s]，还有依赖 %s", t.getTaskName(), task.getTaskName(), t.getDependsOn());
                    }
                }
            }
            mRocket.getLogger().d("[Rocket队列] 任务 [%s] 重整可执行队列完成，当前可执行队列 %s", task.getTaskName(), mRunnableTaskQueue);
        }
    }

    /**
     * 获取下一个可执行的任务
     * RUNNABLE -> RUNNING
     *
     * @return
     * @throws InterruptedException
     */
    Task takeNextRunnableTask() throws InterruptedException {
        Task nextRunningTask = mRunnableTaskQueue.take();
        nextRunningTask.notifyStart();
        nextRunningTask.setTaskStatus(TaskRunStatus.RUNNING);
        mTasksByRunOrder.add(nextRunningTask);
        mRocket.getLogger().d("[Rocket队列] 任务 [%s] 出队，当前可执行队列 %s", nextRunningTask.getTaskName(), mRunnableTaskQueue);
        return nextRunningTask;
    }

    /**
     * 任务标记完成并通知其他依赖的任务
     * RUNNING -> COMPLETE
     *
     * @param task
     */
    void completeTaskAndNotify(Task task) {
        synchronized (mMutexForRunning2Complete) {
            //标记结束，并且把依赖这个任务的其他任务的依赖列表中移除
            task.setTaskStatus(TaskRunStatus.COMPLETE);
            task.notifyEnd();
        }
    }

    Task getTask(String taskName) {
        return mAllTasks.get(taskName);
    }

    /**
     * 如果任务全部结束，停止线程并通知外部
     */
    void judgeToStopQueueAndNotify() {
        boolean allComplete = true;
        for (Map.Entry<String, Task> entry : mAllTasks.entrySet()) {
            Task t = entry.getValue();
            if (t.getTaskStatus() != TaskRunStatus.COMPLETE) {
                allComplete = false;
                break;
            }
        }
        if (allComplete) {
            mIsTaskQueueEnd = true;
            stop();
            notifyTaskQueueEnd();
        }
    }

    /**
     * 提高一个任务及其依赖任务的优先级
     *
     * @param taskName
     */
    void raisePriorityIfNeed(final String taskName) {
        Set<String> deps = new HashSet<>();
        Set<String> needTasks = new HashSet<>();
        findAllDependsOn(taskName, deps);
        needTasks.add(taskName);
        needTasks.addAll(deps);
        updateNeedImmediatelyStatus(needTasks);
        mRocket.getLogger().d("[Rocket队列] 任务 [%s] 及其依赖任务 %s 请求提高优先级", taskName, deps);
        // 这个task包括这个task依赖的task都需要提高优先级
        for (String tn : deps) {
            raisePriorityIfNeedForSingleTask(tn);
        }
        raisePriorityIfNeedForSingleTask(taskName);
        mRocket.getLogger().d("[Rocket队列] 任务 [%s] 及其依赖任务提高优先级完成，当前可执行队列 %s", taskName, mRunnableTaskQueue);
    }

    private void updateNeedImmediatelyStatus(Set<String> deps){
        for(String value: deps){
            getTask(value).setNeedImmediately(true);
        }
    }



    /**
     * 找到一个任务依赖的所有任务
     *
     * @param taskName
     * @param allDeps
     */
    private void findAllDependsOn(String taskName, Set<String> allDeps) {
        final Task task = getTask(taskName);
        Set<String> deps = task.getDependsOn();
        if (deps != null && !deps.isEmpty()) {
            Set<String> copyDeps = new HashSet<>(deps);
            allDeps.addAll(copyDeps);
            for (String depsName : copyDeps) {
                findAllDependsOn(depsName, allDeps);
            }
        }
    }

    /**
     * 提高单个任务的优先级
     *
     * @param taskName
     */
    private void raisePriorityIfNeedForSingleTask(final String taskName) {
        final Task task = getTask(taskName);
        synchronized (mMutexForWating2Runnable) {
            if (Objects.requireNonNull(task).getTaskStatus() == TaskRunStatus.WAITING) {
                // 如果任务还在等待执行，直接提高优先级
                task.raisePriority();
                mRocket.getLogger().d("[Rocket队列] 任务 [%s] 在等待队列中，直接提高优先级 [%s] 成功", task.getTaskName(), task.getPriority());
                return;
            }
        }
        if (task.getTaskStatus() == TaskRunStatus.RUNNABLE) {
            boolean updateResult = mRunnableTaskQueue.update(new Callable<Task>() {
                @Override
                public Task call() throws Exception {
                    task.raisePriority();
                    return task;
                }
            });
            if (!updateResult) {
                mRocket.getLogger().d("[Rocket队列] 任务 [%s] 在可执行队列中，提高优先级 [%s] 失败（可能已经在执行了）", task.getTaskName(), task.getPriority());
            } else {
                mRocket.getLogger().d("[Rocket队列] 任务 [%s] 在可执行队列中，提高优先级 [%s] 成功", task.getTaskName(), task.getPriority());
            }
            return;
        }
        mRocket.getLogger().d("[Rocket队列] 任务 [%s] 已经执行或完成，不需要提高优先级", task.getTaskName());

    }

    /**
     * 确保任务完成
     *
     * @param taskName
     * @throws InterruptedException
     */
    void ensureTask(final String taskName) throws InterruptedException {
        final Task task = getTask(taskName);
        mRocket.getLogger().d("[Rocket队列] 请求确保任务 [%s] 完成", Objects.requireNonNull(task).getTaskName());
        final TaskCompleteEmitterImpl taskCompleteEmitter = new TaskCompleteEmitterImpl();
        synchronized (mMutexForRunning2Complete) {
            if (task.getTaskStatus() == TaskRunStatus.COMPLETE) {
                mRocket.getLogger().d("[Rocket队列] 任务 [%s] 已经完成，直接放行", Objects.requireNonNull(task).getTaskName());
                return;
            }
            mRocket.getLogger().d("[Rocket队列] 任务 [%s] 未完成，开始等待...", Objects.requireNonNull(task).getTaskName());
            registerTaskListener(taskName, new Task.TaskSimpleListener() {
                @Override
                public void onTaskEnd(Task task) {
                    taskCompleteEmitter.onComplete();
                    unregisterTaskListener(taskName, this);
                }
            });
        }
        taskCompleteEmitter.waiting();
        mRocket.getLogger().d("[Rocket队列] 收到任务 [%s] 完成通知，放行", Objects.requireNonNull(task).getTaskName());
    }

    public interface TaskQueueListener {
        void onTaskQueueStart(Rocket rocket);

        void onTaskQueueEnd(Rocket rocket, List<Task> tasksByRunOrder);
    }

    public static class TaskQueueSimpleListener implements TaskQueueListener {

        @Override
        public void onTaskQueueStart(Rocket rocket) {

        }

        @Override
        public void onTaskQueueEnd(Rocket rocket, List<Task> tasksByRunOrder) {

        }
    }

    private final Vector<TaskQueueListener> mTaskQueueListeners = new Vector<>();

    void registerTaskQueueListener(TaskQueueListener l) {
        mTaskQueueListeners.add(l);
    }

    void unregisterTaskQueueListener(TaskQueueListener l) {
        mTaskQueueListeners.remove(l);
    }

    private void notifyTaskQueueStart() {
        Object[] copyed = mTaskQueueListeners.toArray();
        for (Object taskQueueEndListener : copyed) {
            ((TaskQueueListener) taskQueueEndListener).onTaskQueueStart(mRocket);
        }
    }

    private void notifyTaskQueueEnd() {
        Object[] copyed = mTaskQueueListeners.toArray();
        for (Object taskQueueEndListener : copyed) {
            ((TaskQueueListener) taskQueueEndListener).onTaskQueueEnd(mRocket, new ArrayList<>(mTasksByRunOrder));
        }
    }

    void registerTaskListener(String taskName, Task.TaskListener l) {
        Objects.requireNonNull(getTask(taskName)).register(l);
    }

    void unregisterTaskListener(String taskName, Task.TaskListener l) {
        Objects.requireNonNull(getTask(taskName)).unregister(l);
    }

    /**
     * dump当前任务运行快照
     *
     * @return
     */
    List<TaskSnapshot> dumpTaskSnapshots() {
        List<TaskSnapshot> taskSnapshots = new ArrayList<>();
        for (Map.Entry<String, Task> entry : mAllTasks.entrySet()) {
            Task task = entry.getValue();
            if (task.getTaskStatus() == TaskRunStatus.RUNNING && task.getRunningThread() != null) {
                taskSnapshots.add(new TaskSnapshot(task.getTaskName(), task.getTaskStatus(), ThreadUtil.getThreadStackTrace(task.getRunningThread())));
            } else {
                taskSnapshots.add(new TaskSnapshot(task.getTaskName(), task.getTaskStatus(), Collections.<String>emptyList()));
            }
        }
        return taskSnapshots;
    }

    boolean isTaskQueueEnd() {
        return mIsTaskQueueEnd;
    }

    boolean isTaskEnd(String taskName){
        Task task = getTask(taskName);
        return task.getTaskStatus() == TaskRunStatus.COMPLETE;
    }


}
