package com.example.study.init.task

import com.example.study.init.BaseTask
import com.example.study.matrix.DynamicConfigImplDemo
import com.example.study.matrix.TestPluginListener
import com.example.study.utils.ChoreographerMonitor
import com.example.study.utils.LooperMonitor
import com.tencent.matrix.Matrix
import com.tencent.matrix.batterycanary.BatteryMonitorPlugin
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorCallback
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorConfig
import com.tencent.matrix.batterycanary.monitor.feature.JiffiesMonitorFeature
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig

/**
 * Created by chenyy on 2022/7/11.
 */

class MatrixTask : BaseTask("MatrixTask", setOf()) {

    override fun run() {
        LooperMonitor.start()
        val builder: Matrix.Builder = Matrix.Builder(getAppApplication()) // build matrix
        builder.pluginListener(TestPluginListener(getAppApplication()!!)) // add general pluginListener
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
                .enableAnrTrace(true)
                .enableIdleHandlerTrace(true)
                .isDebug(true)
                .enableStartup(true)
                .splashActivities("com.example.study.MainActivity")
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
}