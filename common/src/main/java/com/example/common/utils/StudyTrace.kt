package com.example.common.utils

import android.util.Log

/**
 * 描述:
 * 作者: ChenYy
 * 日期: 2022/4/8 15:30
 */
object StudyTrace {

    private val mTraceStartMap = mutableMapOf<String, Long>()

    private val mTraceEndMap = mutableMapOf<String, Long>()

    fun start(traceName: String) {
        mTraceStartMap[traceName] = System.currentTimeMillis()
    }

    fun end(traceName: String) {
        mTraceEndMap[traceName] = System.currentTimeMillis()
        val time = (mTraceEndMap[traceName] ?: 0) - (mTraceStartMap[traceName] ?: 0)
        Log.d("StudyTrace", "${traceName}:${time}ms")
    }
}