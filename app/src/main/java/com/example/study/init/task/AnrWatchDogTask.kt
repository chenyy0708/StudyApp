package com.example.study.init.task

import com.example.study.init.BaseTask
import com.example.study.logD
import com.example.study.logE
import com.github.anrwatchdog.ANRWatchDog
import com.github.anrwatchdog.ANRWatchDog.ANRListener


/**
 * Created by chenyy on 2022/7/11.
 */

class AnrWatchDogTask : BaseTask("AnrWatchDogTask", setOf()) {

    override fun run() {
        val anrWatchDog = ANRWatchDog(5000)
        anrWatchDog.setANRListener {
            logE("ANR-Watchdog-Demo", "Detected Application Not Responding!")
            logE("ANR-Watchdog-Demo", it.message ?: "--" + it.localizedMessage)
        }
        anrWatchDog.start()
    }
}