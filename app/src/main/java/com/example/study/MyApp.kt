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


/**
 * Created by chenyy on 2021/5/31.
 */
@HiltAndroidApp
class MyApp : Application() {

    companion object {
        var instance: Application? = null
    }

    override fun attachBaseContext(base: Context?) {
        TimeMonitor.startRecord("launch_app", System.currentTimeMillis())
        TimeMonitor.startRecord("startApp", System.currentTimeMillis())
        Trace.beginSection("Application.start")
//        Debug.startMethodTracing()//dmtrace.trace
        instance = this
        super.attachBaseContext(base)
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)
        // 创建RootHandler
        val rootHandler = DefaultRootUriHandler(this)
        // 初始化
        Router.init(rootHandler)

        Thread.sleep(150)

        Looper.getMainLooper()?.queue?.addIdleHandler(object : MessageQueue.IdleHandler {
            override fun queueIdle(): Boolean {
                logD("addIdleHandler:${System.currentTimeMillis()}ms")
                return false
            }
        })
//        Debug.stopMethodTracing()


        val builder: Matrix.Builder = Matrix.Builder(this) // build matrix
        builder.pluginListener(TestPluginListener(this)) // add general pluginListener
        val dynamicConfig = DynamicConfigImplDemo() // dynamic config
        val ioCanaryPlugin = IOCanaryPlugin(
            IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build()
        )
        val fpsPlugin = TracePlugin(
            TraceConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .enableFPS(true)
                .isDebug(true)
                .enableStartup(true)
                .build()
        )
        builder.plugin(fpsPlugin)
        builder.plugin(ioCanaryPlugin)
        val config = BatteryMonitorConfig.Builder()
            .enable(JiffiesMonitorFeature::class.java)
            .enableStatPidProc(true)
            .greyJiffiesTime(30 * 1000L)
            .setCallback(BatteryMonitorCallback.BatteryPrinter())
            .build()
        val plugin = BatteryMonitorPlugin(config)
        builder.plugin(plugin)
        Matrix.init(builder.build())
        ioCanaryPlugin.start()
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationProxy.onCreate(this)
        TimeMonitor.endRecord("startApp", System.currentTimeMillis())
        Trace.endSection()
    }
}