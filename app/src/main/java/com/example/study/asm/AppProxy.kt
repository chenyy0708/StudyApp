package com.example.study.asm

import android.app.Application
import com.example.common.annotation.AppLike
import com.example.common.utils.ModuleProvider

/**
 * Created by chenyy on 2021/6/28.
 */

@AppLike
object AppProxy {
    fun onCreate(application: Application) {
        ModuleProvider.init()
    }
}