package com.example.study.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.study.databinding.ActivityCoroutineBinding
import com.example.study.logD
import com.sankuai.waimai.router.annotation.RouterUri
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Created by chenyy on 2021/5/28.
 */

@RouterUri(path = ["/coroutine"])
class CoroutineActivity : AppCompatActivity() {

    // 将类方法转成lambda
    private val emitFun = TestA::print as Function2<TestA, String, Unit>

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
        var flow = flow<String> {
            val time = System.currentTimeMillis()
            logD("send------name:${Thread.currentThread().name}")
            emit("1000")  // 发送线程如果调用flow.flowOn(xxx)则使用第一个flowOn指定的调度器线程发送，如果没有调用则和collect共用同一个调度器线程
            delay(500)
            logD("time:${System.currentTimeMillis() - time}------name:${Thread.currentThread().name}")
            emit("1001")
        }.flowOn(Dispatchers.IO)

        val testA = TestA()

        (1..3).asFlow()

        emitFun(testA, "params1")
        lifecycleScope.launch(coroutineExceptionHandler + Dispatchers.Default) {

            withContext(Dispatchers.Main) { // 外层的CoroutineContext控制collect方法的线程

                flow
                    // 1. 调用collect其实是对map返回的新的Flow进行收集数据，
                    // 会走到map->transform->unsafeFlow中传入的lambda中，对原始的flow进行collect
                    // 进而触发初始的FlowCollect调用emit方法发送原始数据value = 1000

                    // 2. map->transform->unsafeFlow->collect拿到发送的原始数据1000，会调用unsafeTransform传入的transform lambda表达式，其实就是map->transform传入的lambda
                    // 3. map->transform->lambda中会调用FlowCollect.emit方法，这个FlowCollect对象其实就是最终我们调用的flow.collect {} 生成的。emit发送的值也会通过flow.collect{}传入的lambda接收
                    .map { // map方法会返回一个新的Flow对象
                        logD("map1:${it}------name:${Thread.currentThread().name}")
                        "$it---map1"
                    }
                    .flowOn(Dispatchers.IO) // 每一个flowOn指定的线程控制上面的操作符作用线程，并且第一个flowOn可以控制发送数据所在的线程
                    .onCompletion {
                        logD("Flow onCompletion:${it?.message ?: ""}")
                    }
                    .catch {
                        logD("Flow catch:${it.message ?: ""}")
                    }
                    .apply {
                        logD("Flow name:${this.javaClass.simpleName}")
                    }
//                    .map {
//                        logD("map2------name:${Thread.currentThread().name}")
//                        "$it---map2"
//                    }.flowOn(Dispatchers.IO)
//                    .map {
//                        logD("map3------name:${Thread.currentThread().name}")
//                        "$it---map3"
//                    }
//                    .flowOn(Dispatchers.Default)
                    .collect {
                        logD("Flow${it}---collect:${Thread.currentThread().name}")
                    }
            }
//            delay(1)
//            logD("start")
//            withContext(Dispatchers.Default) {
//                println("withContext start")
//                delay(1000)
//                println("withContext end")
//            }
//            logD("end")
//            throw FileNotFoundException("lifecycleScope.start")
//            val testjob = launch {
//                logD("launch start")
////                throw FileNotFoundException("launch")
//                logD("launch end")
//            }
//            val test = async {
//                logD("async start")
//                delay(100)
////                throw FileNotFoundException("async")
//                logD("async end")
//            }
//            logD("async run")
//            test.await()
//            logD("async run complete")
//            throw FileNotFoundException("lifecycleScope.end")
        }

//        job.cancel()
    }


    class TestA {
        val action: (value: String) -> Unit = {
            logD("action${it}")
        }

        fun print(str: String) = action(str)
    }
}

