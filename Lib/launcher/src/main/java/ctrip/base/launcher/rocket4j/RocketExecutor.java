package ctrip.base.launcher.rocket4j;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ctrip.base.launcher.rocket4j.exception.ExceptionData;
import ctrip.base.launcher.rocket4j.exception.LauncherExceptionHelper;

public class RocketExecutor implements Executor {

    private final Deque<Runnable> readyAsyncCalls = new ArrayDeque<>();

    private ExecutorService executorService;

    public volatile boolean isStopRunnableDelegate = false;

    private RocketExecutor(){

    }

    public static RocketExecutor getInstance(){
        return RocketExecutorHolder.instance;
    }

    private static class RocketExecutorHolder {
        private static final RocketExecutor instance = new RocketExecutor();
    }

    private synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), threadFactory("RocketExecutor Dispatcher", false));
        }
        return executorService;
    }

    private ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    @Override
    public void execute(Runnable command) {
        if(command == null){
            return;
        }
        if(isStopRunnableDelegate){
            try {
                executorService().execute(command);
            }catch (Exception e){
                LauncherExceptionHelper.boom(ExceptionData.createBuilder("RocketExecutor", "ibu.rocket.executor.task.fail").addException(e).get());
            }
        }else {
            readyAsyncCalls.add(command);
        }
    }

    public void startOneTask(){
        Runnable runnable = readyAsyncCalls.pollFirst();
        if(runnable == null){
            return;
        }
        //if (ContextHolder.sDebug) {
            Log.d("RocketExecutor", "当前线程池正在执行"+runnable+"当前线程池剩余执行队列数量"+readyAsyncCalls.size());
        //}
        try {
            executorService().execute(runnable);
        }catch (Exception e){
            LauncherExceptionHelper.boom(ExceptionData.createBuilder("startOneTask", "ibu.rocket.executor.task.fail").addException(e).get());
        }
    }

    public boolean isTaskComplete(){
        return readyAsyncCalls.isEmpty();
    }
}
