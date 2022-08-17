package com.example.launcher.rocket4j;

import com.example.launcher.rocket4j.exception.ExceptionData;
import com.example.launcher.rocket4j.exception.LauncherExceptionHelper;

public class RocketLock {

    private  boolean mIsPaused;

    private final Object object = new Object();

    boolean isPaused() {
        return mIsPaused;
    }


    void unlock(){
        synchronized (object) {
            mIsPaused = false;
            object.notifyAll();
        }
    }

    void lock(){
        synchronized (object) {
            mIsPaused = true;
        }
    }

    void await()  {
        synchronized (object) {
            if(mIsPaused){
                try {
                    object.wait(500);
                } catch (InterruptedException e) {
                    LauncherExceptionHelper.boom(ExceptionData.createBuilder("RocketLock", "ibu.rocket.lock.wait.fail").addException(e).get());
                }
            }
        }
    }


}