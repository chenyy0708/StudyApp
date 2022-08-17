package com.example.study.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.example.study.logD

/**
 * Created by chenyy on 2022/7/19.
 * 内存泄露测试单例类
 */

class SingleTest private constructor() {

    private var context: Context? = null

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: SingleTest? = null

        fun getInstance(): SingleTest {
            if (instance == null) {
                synchronized(SingleTest::class.java) {
                    if (instance == null) {
                        instance = SingleTest()
                    }
                }
            }
            return instance!!
        }
    }

    fun clear() {
        this.context = null
    }

    fun init(context: Context?) {
        logD(tag = "SingleTest", msg = "setContext:${context}")
        this.context = context
    }

    fun showToast(msg: String?) {
        logD(tag = "SingleTest", msg = "showToast:${context}----${msg}")
        if (context != null) {
            Toast.makeText(context, msg ?: "", Toast.LENGTH_SHORT).show()
        }
    }
}