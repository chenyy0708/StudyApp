package com.example.study.utils

import com.example.study.logD

/**
 * Created by chenyy on 2022/7/7.
 */

object TimeMonitor {

    private var mStartTimes: HashMap<String, Long>? = null
    private var mEndTimes: HashMap<String, Long>? = null

    /**
     * 记录输入第一个字符的时间
     */
    fun clearTime(key: String?) {
        mStartTimes?.remove(key)
        mEndTimes?.remove(key)
    }

    /**
     * 记录输入第一个字符的时间
     */
    fun startRecord(key: String?, time: Long?) {
        if (mStartTimes == null) {
            mStartTimes = HashMap()
        }
        mStartTimes?.put(key ?: "", time ?: 0)
    }

    /**
     * 记录输入最后一个字符的时间
     */
    fun endRecord(key: String?, time: Long?) {
        if (mEndTimes == null) {
            mEndTimes = HashMap()
        }
        mEndTimes?.put(key ?: "", time ?: 0)
        recordInputTime(key)
    }

    /**
     * 获取输入第一个和最后一个字符时间差
     */
    private fun getInputTime(key: String?): Long {
        var time = 0L
        val start = mStartTimes?.get(key) ?: 0L
        val end = mEndTimes?.get(key) ?: 0L
        time = if (start == 0L && end != 0L) { // 用户复制粘贴输入验证码
            -1
        } else {
            end - start
        }
        return time
    }

    /**
     * 记录输入验证码时间埋点
     */
    private fun recordInputTime(key: String?) {
        val time = getInputTime(key)
        logD(msg = "time:${time},type:${key}")
    }
}