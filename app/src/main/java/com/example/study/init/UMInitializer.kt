package com.example.study.init

import android.content.Context
import androidx.startup.Initializer

/**
 * Created by chenyy on 2021/7/12.
 */

class UMInitializer : Initializer<UM> {
    override fun create(context: Context): UM {
        Thread.sleep(200L)
        return UM()
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}