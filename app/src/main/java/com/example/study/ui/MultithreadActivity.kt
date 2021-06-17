package com.example.study.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study.R
import com.example.study.logD
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

/**
 * Created by chenyy on 2021/5/28.
 */

class MultithreadActivity : AppCompatActivity() {
    private val reentrantLock = ReentrantLock()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        val runnable = Runnable {
            buyTicket()
        }

        for (i in 1..10) {
            thread(name = "售票员：${i}") {
                runnable.run()
            }
        }

        countDownLatch()
        semaphore()

    }

    private fun semaphore() {
        val start = System.currentTimeMillis()
        logD("semaphore1")
        val semaphore = Semaphore(3, true)
        for (i in 1..5) {
            thread {
                // 获取许可证
                semaphore.acquire(2)
                logD("开始游玩:${i}")
                Thread.sleep(i * 100L)
                logD("结束:${i}")
                semaphore.release(2)
            }
        }
        logD("semaphore2:${System.currentTimeMillis() - start}")
    }

    private fun countDownLatch() {
        val countDownLatch = CountDownLatch(5)
        val start = System.currentTimeMillis()
        logD("countDownLatch1")
        for (i in 0..4) {
            Executors.newCachedThreadPool().execute {
                logD("Executors-Thread:${Thread.currentThread().name}")
                Thread.sleep(1 * 1000L)
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        logD("countDownLatch2:${System.currentTimeMillis() - start}")
    }

    private fun buyTicket() {
        try {
            // 阻塞
            reentrantLock.lock()
            logD("${Thread.currentThread().name}：准备好了")
            Thread.sleep(1000)
            logD("${Thread.currentThread().name}：买好了")
        } finally {
            // 释放
            reentrantLock.unlock()
        }
    }
}