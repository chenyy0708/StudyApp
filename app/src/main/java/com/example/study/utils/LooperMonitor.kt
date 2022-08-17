package com.example.study.utils

import android.os.Looper
import android.os.SystemClock
import android.util.Printer
import com.example.study.logD

/**
 * Created by chenyy on 2022/7/13.
 */

object LooperMonitor {

    fun start() {
        Looper.getMainLooper().setMessageLogging(StudyLooperPrinter())
    }

    class StudyLooperPrinter : Printer {
        // 一次事件开始时间
        private var mThisEventStartTime: Long = 0
        private var mThisEventStartThreadTime: Long = 0

        private val START = ">>>>> Dispatching"
        private val END = "<<<<< Finished"


        override fun println(log: String?) {
            log ?: return
            val start: Int = log.indexOf("} ")
            val end: Int = log.indexOf("@", start)
            if (start < 0 || end < 0) {
                return
            }
            if (log.startsWith(START)) {
                // 事件开始
                mThisEventStartTime = System.currentTimeMillis()
                mThisEventStartThreadTime = SystemClock.currentThreadTimeMillis();
            }
            if (log.startsWith(END)) {
                if (mThisEventStartTime == 0L || mThisEventStartThreadTime == 0L) return
                val thisEventEndTime = System.currentTimeMillis()
                val thisEventThreadEndTime = SystemClock.currentThreadTimeMillis()
                val eventCostTime = thisEventEndTime - mThisEventStartTime
                val eventCostThreadTime = thisEventThreadEndTime - mThisEventStartThreadTime
                if (eventCostTime >= LogMonitor.TIME_BLOCK) {
                    logD(
                        tag = "LogMonitor",
                        msg = "事件执行事件：eventCostTime:${eventCostTime}，eventCostThreadTime:${eventCostThreadTime}，\n log:${log}"
                    )
                }
            }
            if (log.startsWith(START)) {
                LogMonitor.getInstance().startMonitor();
            }
            if (log.startsWith(END)) {
                LogMonitor.getInstance().removeMonitor();
            }
        }
    }
}