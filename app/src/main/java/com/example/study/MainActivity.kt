package com.example.study

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.example.study.databinding.ActivityMainBinding
import com.example.study.ui.ComponentActivity
import com.example.study.ui.MultithreadActivity
import com.example.study.ui.RVActivity
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(200)
        setContentView(R.layout.activity_main)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            logD("exception:${throwable.message}")
        }

        lifecycleScope.launchWhenResumed {
            whenResumed {  }
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

}