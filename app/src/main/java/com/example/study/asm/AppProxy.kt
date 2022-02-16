package com.example.study.asm

import android.app.Application
import com.example.common.annotation.AppLike
import com.example.common.init.ModuleHelper

/**
 * Created by chenyy on 2021/6/28.
 */

@AppLike
object AppProxy {
    fun onCreate(application: Application) {
//        ServiceLoader.onCreate()
        ModuleHelper.init(application)
    }
}