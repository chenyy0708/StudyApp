package ctrip.base.launcher.rocket4j;

import java.util.List;

import ctrip.base.launcher.rocket4j.util.Log4Rocket;
import ctrip.base.launcher.rocket4j.util.StringUtil;

public class Rocket {

    private boolean mIsLaunched;
    private TaskQueue mTaskQueue;
    private Log4Rocket mLog4Rocket;
    RocketLock mRocketLock;
    private Config mConfig;


    public static Rocket newInstance(Config config) {
        return new Rocket(config);
    }

    private Rocket(Config config) {
        if (config == null || !config.isValid()) {
            throw new IllegalArgumentException(String.format("Config %s not valid.", config));
        }
        mIsLaunched = false;
        mRocketLock = new RocketLock();
        mLog4Rocket = new Log4Rocket(String.format("[%s]", config.getName()), config.getLogger());
        mTaskQueue = new TaskQueue(this, config);
        mConfig = config;
    }

    public Config getConfig() {
        return mConfig;
    }

    public void setConfig(Config mConfig) {
        this.mConfig = mConfig;
    }

    public Rocket launch() {
        synchronized (this) {
            if (mIsLaunched) {
                getLogger().d("Rocket has launched before.");
                return this;
            }
            mTaskQueue.start();
            mIsLaunched = true;
            return this;
        }
    }

    public void resume(){
        mRocketLock.unlock();
    }

    public void pause(){
        mRocketLock.lock();
    }





    /**
     * 确保任务完成
     *
     * @param taskName
     * @throws InterruptedException
     */
    public void ensureTask(String taskName) throws InterruptedException {
        if (!mIsLaunched) {
            launch();
        }
        mTaskQueue.raisePriorityIfNeed(taskName);
        mRocketLock.unlock();
        mTaskQueue.ensureTask(taskName);
    }

    /**
     * 确保任务完成
     *
     * @param taskNames
     * @throws InterruptedException
     */
    public void ensureTasks(String... taskNames) throws InterruptedException {
        if (!mIsLaunched) {
            launch();
        }
        for (String taskName : taskNames) {
            mTaskQueue.raisePriorityIfNeed(taskName);
        }
        mRocketLock.unlock();
        for (String taskName : taskNames) {
            mTaskQueue.ensureTask(taskName);
        }
    }



    /**
     * 获取当前任务运行快照
     *
     * @return
     */
    public List<TaskSnapshot> dumpTaskSnapshots() {
        return mTaskQueue.dumpTaskSnapshots();
    }

    /**
     * rocket是否完成
     *
     * @return
     */
    public boolean isRocketComplete() {
        return mTaskQueue.isTaskQueueEnd();
    }

    public void registerTaskQueueListener(TaskQueue.TaskQueueListener l) {
        mTaskQueue.registerTaskQueueListener(l);
    }

    public void unregisterTaskQueueListener(TaskQueue.TaskQueueListener l) {
        mTaskQueue.unregisterTaskQueueListener(l);
    }

    public void registerTaskListener(String taskName, Task.TaskListener l) {
        mTaskQueue.registerTaskListener(taskName, l);
    }

    public void unregisterTaskListener(String taskName, Task.TaskListener l) {
        mTaskQueue.unregisterTaskListener(taskName, l);
    }

    public Log4Rocket getLogger() {
        return mLog4Rocket;
    }

    public static class Config {
        private String mName = "Rocket4J";
        private Log4Rocket.Logger mLogger;
        private int mThreadPoolSize;
        private List<Task> mTasks;

        public int getThreadPoolSize() {
            return mThreadPoolSize;
        }

        public Config setThreadPoolSize(int threadPoolSize) {
            this.mThreadPoolSize = threadPoolSize;
            return this;
        }

        public List<Task> getTasks() {
            return mTasks;
        }

        public Config setTasks(List<Task> tasks) {
            mTasks = tasks;
            return this;
        }

        public String getName() {
            return mName;
        }

        public Config setName(String name) {
            mName = name;
            return this;
        }

        public Log4Rocket.Logger getLogger() {
            return mLogger;
        }

        public Config setLogger(Log4Rocket.Logger logger) {
            mLogger = logger;
            return this;
        }

        public boolean isValid() {
            return !StringUtil.isEmpty(mName) && mThreadPoolSize > 0 && mTasks != null && !mTasks.isEmpty();
        }

        @Override
        public String toString() {
            return "Config{" +
                    "mName='" + mName + '\'' +
                    ", mLogger=" + mLogger +
                    ", mThreadPoolSize=" + mThreadPoolSize +
                    ", mTasks=" + mTasks +
                    '}';
        }
    }
}