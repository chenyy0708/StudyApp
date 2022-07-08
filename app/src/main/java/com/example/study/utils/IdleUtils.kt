package com.example.study.utils

import android.os.Looper
import android.os.MessageQueue
import com.alibaba.android.arouter.launcher.ARouter
import com.example.study.BuildConfig
import com.example.study.MyApp
import com.example.study.logD
import com.example.study.matrix.DynamicConfigImplDemo
import com.example.study.matrix.TestPluginListener
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

object IdleUtils {

    fun execute(runnable: Runnable) {
        Looper.getMainLooper()?.queue?.addIdleHandler(object : MessageQueue.IdleHandler {
            override fun queueIdle(): Boolean {
                // 使用线程池初始化任务
                MyApp.instance?.scheduleSingleTask(runnable)
                return false
            }
        })
    }

    fun getThirdSDKInitTask() : Runnable = Runnable {
        TimeMonitor.startRecord("thirdSDK", System.currentTimeMillis())
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(MyApp.instance)
        // 创建RootHandler
        val rootHandler = DefaultRootUriHandler(MyApp.instance)
        // 初始化
        Router.init(rootHandler)

        Thread.sleep(150)

        Looper.getMainLooper()?.queue?.addIdleHandler(object : MessageQueue.IdleHandler {
            override fun queueIdle(): Boolean {
                logD("addIdleHandler:${System.currentTimeMillis()}ms")
                return false
            }
        })
        val builder: Matrix.Builder = Matrix.Builder(MyApp.instance) // build matrix
        builder.pluginListener(TestPluginListener(MyApp.instance!!)) // add general pluginListener
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
        TimeMonitor.endRecord("thirdSDK", System.currentTimeMillis())
    }
}