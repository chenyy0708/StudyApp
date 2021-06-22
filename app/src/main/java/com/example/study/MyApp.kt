package com.example.study

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.example.study.monitor.LauncherUtils


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
    }
}