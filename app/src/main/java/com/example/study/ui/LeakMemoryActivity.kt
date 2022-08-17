package com.example.study.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.study.databinding.ActivityLeakMemoryBinding
import com.example.study.logD
import com.example.study.utils.SingleTest
import com.sankuai.waimai.router.annotation.RouterUri
import java.lang.ref.WeakReference


/**
 * Created by chenyy on 2021/5/28.
 */

@RouterUri(path = ["/leakMemory"])
class LeakMemoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityLeakMemoryBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    override fun onDestroy() {
        SingleTest.getInstance().clear()
        super.onDestroy()
    }

    fun singleton(view: View) {
        SingleTest.getInstance().init(this.applicationContext)
        SingleTest.getInstance().showToast("单例泄露")
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            logD(tag = "MyHandler", msg = "activity:${this@LeakMemoryActivity}")
            when (msg.what) {
                1001 -> showToast("非静态内部类Handler")
            }
        }
    }

    fun handler(view: View) {
        val message = Message.obtain()
        message.what = 1001
        handler.sendMessageDelayed(message, 30 * 1000L)
    }

    private val handlerV2 = MyHandler(this)

    fun staticHandler(view: View) {
        val message = Message.obtain()
        message.what = 1003
        handlerV2.sendMessageDelayed(message, 30 * 1000L)
    }

    companion object {
        var dog: Dog? = null
    }

    fun innerClass(view: View) {
        dog = Dog()
    }

    inner class Dog {
        fun call() {
            showToast("汪汪汪！")
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private class MyHandler(activity: LeakMemoryActivity?) : Handler(Looper.getMainLooper()) {
        var mainActivityWeakReference: WeakReference<LeakMemoryActivity?>
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            logD(tag = "MyHandler", msg = "activityV2:${mainActivityWeakReference.get()}")
            when (msg.what) {
                1003 -> {
                    mainActivityWeakReference.get()?.showToast("静态Handler")
                }
            }
        }

        init {
            mainActivityWeakReference = WeakReference<LeakMemoryActivity?>(activity)
        }
    }
}
