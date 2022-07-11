package com.example.common.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author yfchu
 * @date 2016/3/17
 */
public class ThreadUtils {

    private static Handler mainHandler;

    private static TaskHandleProxy proxy = null;

    public static void setTaskProxy(TaskHandleProxy taskProxy) {
        proxy = taskProxy;
    }

    /**************************************/
    /**  Work Thread Pool, 适合计算型任务，比如JSON解析，单个任务时间不宜太长 **/
    /**************************************/
    public static void runOnBackgroundThread(Runnable runnable) {
        if (proxy != null) {
            proxy.proxy(runnable);
        } else {
            WorkHolder.workService.execute(runnable);
        }
    }

    public static void runOnBackgroundThread(Runnable runnable, long delay) {
        WorkHolder.delayWorkService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**************************************/
    /**  IO Thread Pool, 适合IO型任务，比如网络，文件 **/
    /**************************************/
    private static class IoHolder {
        public static ExecutorService ioService = new ThreadPoolExecutor(0, 1024,
                5L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public static void runOnIOThread(Runnable runnable) {
        IoHolder.ioService.execute(runnable);
    }


    /**************************************/
    /**  Timer Thread Pool **/
    /**************************************/
    public static void runOnTimerThread(Runnable runnable, long delay) {
        WorkHolder.timerService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }


    /**************************************/
    /**  Main Thread **/
    /**************************************/
    private static void internalRunOnUiThread(Runnable runnable, long delayMillis) {
        getMainHandler();
        mainHandler.postDelayed(runnable, delayMillis);
    }

    public static void post(Runnable runnable) {
        getMainHandler().post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        getMainHandler().postDelayed(runnable, delayMillis);
    }

    public static void runOnUiThread(Runnable runnable) {
        internalRunOnUiThread(runnable, 0);
    }

    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        internalRunOnUiThread(runnable, delayMillis);
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    public static void removeCallback(Runnable runnable) {
        getMainHandler().removeCallbacks(runnable);
    }

    private static class WorkHolder {
        public static ThreadPoolExecutor workService = new ThreadPoolExecutor(10, Integer.MAX_VALUE,
                3L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "ThreadUtils workService");
            }
        });

        public static ScheduledExecutorService delayWorkService = Executors.newScheduledThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "ThreadUtils delayWorkService");
            }
        });

        public static ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "ThreadUtils timerService");
            }
        });

        static {
            workService.allowCoreThreadTimeOut(true);
        }
    }

    public static String getStackTraceString(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements == null || stackTraceElements.length == 0) {
            return "";
        }
        StringBuilder stackBuffer = new StringBuilder();
        for (StackTraceElement traceElement : stackTraceElements) {
            if (traceElement == null) {
                stackBuffer.append("(empty)");
            } else {
                stackBuffer.append(traceElement);
            }
            stackBuffer.append("\n");
        }
        return stackBuffer.toString();
    }

    public interface TaskHandleProxy {
        void proxy(Runnable runnable);
    }

}
