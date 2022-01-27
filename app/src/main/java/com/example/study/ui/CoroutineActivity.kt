package com.example.study.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.study.databinding.ActivityCoroutineBinding
import com.example.study.logD
import kotlinx.coroutines.*
import java.io.FileNotFoundException
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
 * Created by chenyy on 2021/5/28.
 */

class CoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCoroutineBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            logD("Exception:${throwable.message}")
        }
        val job1 = Job()
//        val job2 = Job()
//        val coroutineContext = Dispatchers.IO + coroutineExceptionHandler  + job1
//
//        val value = coroutineContext[Job.Key]
//        val value2 = coroutineContext[Dispatchers.IO.key]
//
//        val coroutineContext2 = coroutineContext + Dispatchers.Main
//
//        val coroutineContext3 = coroutineContext + job2
        val job = lifecycleScope.launch(coroutineExceptionHandler) {
//            delay(1)
//            logD("start")
//            withContext(Dispatchers.Default) {
//                println("withContext start")
//                delay(1000)
//                println("withContext end")
//            }
//            logD("end")
//            throw FileNotFoundException("lifecycleScope.start")
            val testjob = launch {
                logD("launch start")
//                throw FileNotFoundException("launch")
                logD("launch end")
            }
            val test = async {
                logD("async start")
                delay(100)
//                throw FileNotFoundException("async")
                logD("async end")
            }
            logD("async run")
            test.await()
            logD("async run complete")
            throw FileNotFoundException("lifecycleScope.end")
        }

//        job.cancel()
    }
}