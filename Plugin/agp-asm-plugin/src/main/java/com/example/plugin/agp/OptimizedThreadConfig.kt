package com.example.plugin.agp

/**
 * Created by chenyy on 2022/5/5.
 */

class OptimizedThreadConfig(
    private val optimizedThreadClass: String = "com.example.study.asm.OptimizedThread",
    private val optimizedThreadPoolClass: String = "github.leavesczy.asm.optimizedThread.OptimizedExecutors",
    val threadHookPointList: List<ThreadHookPoint> = threadHookPoints,
) {

    val formatOptimizedThreadClass: String
        get() = optimizedThreadClass.replace(".", "/")

    val formatOptimizedThreadPoolClass: String
        get() = optimizedThreadPoolClass.replace(".", "/")

}

private val threadHookPoints = listOf(
    ThreadHookPoint(
        methodName = "newFixedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadExecutor"
    ),
    ThreadHookPoint(
        methodName = "newCachedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadScheduledExecutor"
    ),
    ThreadHookPoint(
        methodName = "newScheduledThreadPool"
    ),
)

data class ThreadHookPoint(
    val methodName: String
)