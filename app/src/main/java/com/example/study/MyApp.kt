package com.example.study

import android.app.Application
import android.content.Context
import com.example.study.matrix.DynamicConfigImplDemo
import com.example.study.matrix.TestPluginListener
import com.tencent.matrix.Matrix
import com.tencent.matrix.batterycanary.BatteryMonitorPlugin
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorCallback.BatteryPrinter
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorConfig
import com.tencent.matrix.batterycanary.monitor.feature.JiffiesMonitorFeature
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig


/**
 * Created by chenyy on 2021/5/31.
 */

class MyApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        val builder: Matrix.Builder = Matrix.Builder(this) // build matrix
        builder.patchListener(TestPluginListener(this)) // add general pluginListener
        val dynamicConfig = DynamicConfigImplDemo() // dynamic config
        val ioCanaryPlugin = IOCanaryPlugin(
            IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build()
        )
        builder.plugin(ioCanaryPlugin)
        val config = BatteryMonitorConfig.Builder()
            .enable(JiffiesMonitorFeature::class.java)
            .enableStatPidProc(true)
            .greyJiffiesTime(30 * 1000L)
            .setCallback(BatteryPrinter())
            .build()

        val plugin = BatteryMonitorPlugin(config)
        builder.plugin(plugin)
        Matrix.init(builder.build())
        ioCanaryPlugin.start()


    }
}