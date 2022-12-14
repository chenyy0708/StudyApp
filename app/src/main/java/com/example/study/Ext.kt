package com.example.study

import android.util.Log

/**
 * Created by chenyy on 2021/6/15.
 */

fun logD(msg: String, tag: String = "StudyDemo") {
    Log.d(tag, msg)
}

fun logE(tag: String = "StudyDemo", msg: String) {
    Log.e(tag, msg)
}
