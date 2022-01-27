package com.example.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.example.study.databinding.ActivityMainBinding
import com.example.study.init.TaskStartup
import com.example.study.ui.ComponentActivity
import com.example.study.ui.MultithreadActivity
import com.example.study.ui.RVActivity
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import cn.hikyson.godeye.core.exceptions.UninstallException

import cn.hikyson.godeye.core.GodEye
import cn.hikyson.godeye.core.internal.modules.fps.Fps
import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo
import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo
import com.example.study.ui.CoroutineActivity
import io.reactivex.functions.Consumer
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val start = System.currentTimeMillis()
        logD("开始初始化Task")
//        AppInitializer.getInstance(this)
//            .initi2alizeComponent(MapInitializer::class.java)
//        TaskStartup.start()
        logD("初始化Task结束，耗时${System.currentTimeMillis() - start}ms")
        try {
            GodEye.instance().getModule<Fps>(GodEye.ModuleName.FPS).subject()?.subscribe {
//                logD("fwegwerwerw:$" + it.currentFps + "---" + it.systemFps)
            }
        } catch (e: UninstallException) {
            e.printStackTrace()
        }
        Thread.sleep(200)
        setContentView(R.layout.activity_main)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            logD("exception:${throwable.message}")
        }

        lifecycleScope.launchWhenResumed {
            whenResumed { }
        }

        lifecycleScope.launch(coroutineExceptionHandler) {
            logD("step1")
            val result = withContext(Dispatchers.IO) {
                read()
            }
            logD("step2:${result}")
        }
    }

    private suspend fun read(): String = suspendCancellableCoroutine<String> {
        Thread.sleep(1000L)
        logD("Thread:${Thread.currentThread().name}")
        it.resume("fwefwef")
    }

    override fun onResume() {
        super.onResume()
    }

    fun openRv(view: View) {
        startActivity(Intent(this, RVActivity::class.java))
    }

    fun openComponentActivity(view: View) {
        startActivity(Intent(this, ComponentActivity::class.java))
    }

    fun openMultithreading(view: View) {
        startActivity(Intent(this, MultithreadActivity::class.java))
    }

    fun openCoroutine(view: View) {
        startActivity(Intent(this, CoroutineActivity::class.java))
    }

}