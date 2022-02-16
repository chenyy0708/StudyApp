package com.example.plugin.base.utils

/**
 * Created by chenyy on 2022/2/16.
 */

object Log {
    @JvmStatic
    fun info(msg: Any) {
        try {
            println((String.format("{%s}", msg.toString())))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}