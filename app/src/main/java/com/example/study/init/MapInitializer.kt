package com.example.study.init

import android.content.Context
import androidx.startup.Initializer

/**
 * Created by chenyy on 2021/7/12.
 */

class MapInitializer : Initializer<AMap> {
    override fun create(context: Context): AMap {
        Thread.sleep(2000L)
        return AMap()
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(MusicInitializer::class.java,UMInitializer::class.java)
    }
}