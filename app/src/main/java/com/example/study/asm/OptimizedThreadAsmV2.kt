package com.example.study.asm

/**
 * Created by chenyy on 2022/5/5.
 */
class OptimizedThreadAsmV2 {
    fun test() {
        val runnable = Runnable { println("ChenYy") }
        Thread(runnable, "thread name").start()
    }
}