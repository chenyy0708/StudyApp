package com.example.study.asm

import com.example.study.logD

/**
 * Created by chenyy on 2022/5/5.
 */
class OptimizedThreadAsmV2 {
    fun test() {
        val runnable = Runnable {
            logD("ChenYy2:" + Thread.currentThread().name)
        }
        Thread(runnable, "thread name").start()
    }
}