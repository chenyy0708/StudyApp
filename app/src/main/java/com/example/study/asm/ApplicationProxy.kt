package com.example.study.asm

import android.app.Application
import com.example.modulelike.core.ModuleProvider

/**
 * Created by chenyy on 2021/6/28.
 */

object ApplicationProxy {
    fun onCreate(application: Application) {
        ModuleProvider.init()
    }
}