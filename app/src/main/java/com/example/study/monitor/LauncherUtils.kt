package com.example.study.monitor

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Created by chenyy on 2021/6/7.
 */

object LauncherUtils {

    /**
     * attachBaseContext初始化，得到APP进程的启动开始时间
     */
    fun register(application: Application?) {

        application?.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}