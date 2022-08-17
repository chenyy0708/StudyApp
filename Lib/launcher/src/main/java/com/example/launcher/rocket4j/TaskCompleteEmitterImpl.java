package com.example.launcher.rocket4j;

import java.util.concurrent.CountDownLatch;

public class TaskCompleteEmitterImpl implements TaskCompleteEmitter {
    private CountDownLatch mCompletableEmitter;

    public TaskCompleteEmitterImpl() {
        this.mCompletableEmitter = new CountDownLatch(1);
    }

    @Override
    public void onComplete() {
        this.mCompletableEmitter.countDown();
    }

    void waiting() throws InterruptedException {
        this.mCompletableEmitter.await();
    }
}