package com.example.study.utils

import android.view.Choreographer
import com.example.study.logD
import kotlin.math.round

/**
 * Created by chenyy on 2022/7/14.
 */

object ChoreographerMonitor {

    fun start() {
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {

            var firstFrameTimeNanos: Long = 0

            // 最后一帧时间
            var lastFrameTimeNanos: Long = 0

            override fun doFrame(frameTimeNanos: Long) {
                if (firstFrameTimeNanos == 0L) { // 记录第一帧时间
                    firstFrameTimeNanos = frameTimeNanos
                    lastFrameTimeNanos = firstFrameTimeNanos
                } else {
                    val diffTimeNanos = (frameTimeNanos - lastFrameTimeNanos) / 1000000.0
                    if (diffTimeNanos >= LogMonitor.TIME_BLOCK) {
                        logD(
                            tag = "LogMonitor",
                            msg = "每一帧时间差：${diffTimeNanos}，当前帧率：${round(1000 / diffTimeNanos)}"
                        )
                    }
                    lastFrameTimeNanos = frameTimeNanos
                }
                if (LogMonitor.getInstance().isMonitor) {
                    LogMonitor.getInstance().removeMonitor();
                }
                LogMonitor.getInstance().startMonitor()
                Choreographer.getInstance().postFrameCallback(this)
            }
        })
    }
}