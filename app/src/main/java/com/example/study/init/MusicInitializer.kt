package com.example.study.init

import android.content.Context
import androidx.startup.Initializer
import kotlinx.coroutines.delay

/**
 * Created by chenyy on 2021/7/12.
 */

class MusicInitializer : Initializer<Music> {
    override fun create(context: Context): Music {
        Thread.sleep(1000L)
        return Music()
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(UMInitializer::class.java)
    }
}