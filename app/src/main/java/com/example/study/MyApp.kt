package com.example.study

import android.app.Application
import android.content.Context
import android.os.Debug
import android.os.Looper
import android.os.MessageQueue
import androidx.tracing.Trace
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common.utils.StudyTrace
import com.example.study.asm.ApplicationProxy
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler
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
//        Trace.beginSection("startApp")
//        Debug.startMethodTracing()//dmtrace.trace
        StudyTrace.start("startApp")
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
//        Trace.endSection()
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationProxy.onCreate(this)
    }
}