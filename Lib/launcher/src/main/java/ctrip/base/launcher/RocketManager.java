package ctrip.base.launcher;

import android.os.SystemClock;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import ctrip.base.launcher.rocket4j.Rocket;
import ctrip.base.launcher.rocket4j.Task;
import ctrip.base.launcher.rocket4j.TaskQueue;
import ctrip.base.launcher.rocket4j.util.Log4Rocket;

public class RocketManager {

    public static void launchRocket(List<Task> tasks, String rocketName, boolean await) {
        if (tasks == null || tasks.isEmpty()) return;
        long startTime = System.currentTimeMillis();
        Rocket.Config config = new Rocket.Config();
        config.setName(rocketName);
        config.setTasks(tasks);
        config.setThreadPoolSize(Runtime.getRuntime().availableProcessors());
        config.setLogger(new Log4Rocket.Logger() {
            @Override
            public void log(String message) {
                Log.d(rocketName, message);
            }
        });

        Map<String, String> tasksStatusData = new ConcurrentHashMap<>();
        final Rocket appRocket = Rocket.newInstance(config);
        for (Task task : tasks) {
            appRocket.registerTaskListener(task.getTaskName(), new Task.TaskListener() {
                private long start;
                private long threadStart;

                @Override
                public void onTaskStart(Task task) {
                    start = SystemClock.elapsedRealtime();
                    threadStart = SystemClock.currentThreadTimeMillis();
                }

                @Override
                public void onTaskEnd(Task task) {
//                    tasksStatusData.put(task.getTaskName(),(SystemClock.currentThreadTimeMillis() - threadStart) + "");   //线程真实Running时间
                    tasksStatusData.put(task.getTaskName(),(SystemClock.elapsedRealtime() - start) + "");                   //线程存活时间，包含线程挂起时间
                    Log.d(rocketName, String.format(task.getTaskName() + " TaskEnd Thread cost time:%d, task cost time:%d", (SystemClock.currentThreadTimeMillis() - threadStart), (SystemClock.elapsedRealtime() - start)));
                }
            });
        }

        CountDownLatch appLatch = null;
        if (await) {
            appLatch = new CountDownLatch(1);
        }
        CountDownLatch finalAppLatch = appLatch;
        appRocket.registerTaskQueueListener(new TaskQueue.TaskQueueSimpleListener() {
            @Override
            public void onTaskQueueEnd(Rocket rocket, List<Task> tasksByRunOrder) {
                super.onTaskQueueEnd(rocket, tasksByRunOrder);
                if (await) {
                    finalAppLatch.countDown();
                }else{
//                    Map<String, String> rocketStatus = new HashMap<>();
//                    rocketStatus.put("timeCost", String.valueOf(System.currentTimeMillis() - startTime));
//                    rocketStatus.putAll(tasksStatusData);
//                    logUBT(false,rocketName,rocketStatus);
                }
            }
        });

        appRocket.launch();

        if (await) {
            try {
                appLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Map<String, String> status = new HashMap<>();
//            status.put("timeCost", String.valueOf(System.currentTimeMillis() - startTime));
//            status.putAll(tasksStatusData);
//            logUBT(true,rocketName,status);
        }
    }
}
