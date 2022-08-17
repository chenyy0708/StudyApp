package com.example.study

import android.app.Application
import android.content.Context
import android.os.Looper
import android.os.MessageQueue
import androidx.tracing.Trace
import com.alibaba.android.arouter.launcher.ARouter
import com.example.study.asm.ApplicationProxy
import com.example.study.matrix.DynamicConfigImplDemo
import com.example.study.matrix.TestPluginListener
import com.example.study.utils.TimeMonitor
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler
import com.tencent.matrix.Matrix
import com.tencent.matrix.batterycanary.BatteryMonitorPlugin
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorCallback
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorConfig
import com.tencent.matrix.batterycanary.monitor.feature.JiffiesMonitorFeature
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * Created by chenyy on 2021/5/31.
 */
@HiltAndroidApp
class MyApp : Application() {

    companion object {
        var instance: MyApp? = null
    }

    override fun attachBaseContext(base: Context?) {
        TimeMonitor.startRecord("launch_app", System.currentTimeMillis())
        TimeMonitor.startRecord("startApp", System.currentTimeMillis())
        Trace.beginSection("Application.start")
        instance = this
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationProxy.onCreate(this)
        TimeMonitor.endRecord("startApp", System.currentTimeMillis())
        Trace.endSection()
    }

    private val singleTaskQueue: Executor by lazy { Executors.newSingleThreadExecutor() }

    fun scheduleSingleTask(task: Runnable) {
        singleTaskQueue.execute(task)
    }
}