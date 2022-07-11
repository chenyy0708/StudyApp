package com.example.study.init.task

import com.alibaba.android.arouter.launcher.ARouter
import com.example.study.BuildConfig
import com.example.study.init.BaseTask
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler

/**
 * Created by chenyy on 2022/7/11.
 */

class RouterTask : BaseTask("RouterTask", setOf()) {

    override fun run() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(getAppApplication())
        // 创建RootHandler
        val rootHandler = DefaultRootUriHandler(getAppApplication())
        // 初始化
        Router.init(rootHandler)
    }
}