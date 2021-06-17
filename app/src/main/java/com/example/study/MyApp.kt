package com.example.study

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.example.study.monitor.LauncherUtils

//import com.tencent.matrix.Matrix
//import com.tencent.matrix.batterycanary.BatteryMonitorPlugin
//import com.tencent.matrix.batterycanary.monitor.BatteryMonitorCallback.BatteryPrinter
//import com.tencent.matrix.batterycanary.monitor.BatteryMonitorConfig
//import com.tencent.matrix.batterycanary.monitor.feature.JiffiesMonitorFeature
//import com.tencent.matrix.iocanary.IOCanaryPlugin
//import com.tencent.matrix.iocanary.config.IOConfig
//import com.tencent.matrix.resource.ResourcePlugin
//import com.tencent.matrix.resource.config.ResourceConfig
//import com.tencent.matrix.trace.TracePlugin
//import com.tencent.matrix.trace.config.TraceConfig


/**
 * Created by chenyy on 2021/5/31.
 */

class MyApp : Application() {

    companion object{
        var instance:Application? = null
    }

    override fun attachBaseContext(base: Context?) {
        instance = this
        super.attachBaseContext(base)
        LauncherUtils.register(this)
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)
//        val builder: Matrix.Builder = Matrix.Builder(this) // build matrix
//        builder.patchListener(TestPluginListener(this)) // add general pluginListener
//        val dynamicConfig = DynamicConfigImplDemo() // dynamic config
//        // 性能、泄漏全面监控
//        val ioCanaryPlugin = IOCanaryPlugin(
//                IOConfig.Builder()
//                        .dynamicConfig(dynamicConfig)
//                        .build()
//        )
//        builder.plugin(ioCanaryPlugin)
//        // 电量
//        builder.plugin(BatteryMonitorPlugin(
//                BatteryMonitorConfig.Builder()
//                        .enable(JiffiesMonitorFeature::class.java)
//                        .enableStatPidProc(true)
//                        .greyJiffiesTime(30 * 1000L)
//                        .setCallback(BatteryPrinter())
//                        .build()))
//        // 编译期动态修改字节码, 高性能记录执行耗时与调用堆栈
//        builder.plugin(TracePlugin(
//                TraceConfig.Builder()
//                        .enableFPS(true)
//                        .enableStartup(true)
//                        .enableEvilMethodTrace(true)
//                        .enableAnrTrace(true)
//                        .build()
//        ))
//        // Bitmap
//        builder.plugin(ResourcePlugin(
//                ResourceConfig.Builder()
//                        .dynamicConfig(dynamicConfig)
//                        .build()
//        ))
//        // 启动
//        Matrix.init(builder.build())
//        Matrix.with().startAllPlugins()


    }
}