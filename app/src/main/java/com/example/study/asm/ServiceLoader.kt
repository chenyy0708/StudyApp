package com.example.study.asm

import com.example.common.BaseAppLike
import com.example.common.annotation.LikeLoader
import java.util.ArrayList

/**
 * Created by chenyy on 2021/6/28.
 */

@LikeLoader
object ServiceLoader {

    private val moduleLikes = ArrayList<BaseAppLike>()

    fun onCreate() {
        moduleLikes.forEach {
            it.onCreate()
        }
    }
}