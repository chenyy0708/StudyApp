package com.example.study.asm

import android.app.Application
import com.example.modulelike.core.ModuleProvider
import com.example.study.utils.IdleUtils

/**
 * Created by chenyy on 2021/6/28.
 */

object ApplicationProxy {
    fun onCreate(application: Application) {
        ModuleProvider.init()
        IdleUtils.execute(IdleUtils.getThirdSDKInitTask())
    }
}