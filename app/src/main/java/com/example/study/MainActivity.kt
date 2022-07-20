package com.example.study

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.tracing.Trace
import com.example.study.asm.OptimizedThreadAsm
import com.example.study.databinding.ActivityMainBinding
import com.example.study.ui.ComponentActivity
import com.example.study.ui.LeakMemoryActivity
import com.example.study.ui.MultithreadActivity
import com.example.study.ui.RVActivity
import com.example.study.utils.SingleTest
import com.example.study.utils.TimeMonitor
import com.sankuai.waimai.router.Router
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var analyticsService: AnalyticsService

    @Inject
    lateinit var user: User

    @Inject
    lateinit var user2: User

    @Inject
    lateinit var retrofit: Retrofit

    private val viewModel by viewModels<MVM>()


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        Trace.beginSection("MainActivity.onCreate")
        super.onCreate(savedInstanceState)
        TimeMonitor.startRecord("activity_launch", System.currentTimeMillis())
        val start = System.currentTimeMillis()
        analyticsService.analyticsMethods()
        OptimizedThreadAsm().test()
        viewModel.test()
        user.test()
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            llContainer.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    TimeMonitor.endRecord("activity_launch", System.currentTimeMillis())
                    llContainer.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
            })
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            logD("exception:${throwable.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        TimeMonitor.endRecord("launch_app", System.currentTimeMillis())
        reportFullyDrawn()
        Trace.endSection()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
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
        Router.startUri(this, "/coroutine")
    }

    fun openCompose(view: View) {
        Router.startUri(this, "/compose")
    }

    fun payActivity(view: View) {
        Router.startUri(this, "/pay")
    }

    val content =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { success: Map<String, Boolean>? ->
            Toast.makeText(this, "权限请求:${success}", Toast.LENGTH_SHORT).show()
        }

    fun requestPermission(view: View) {
        content.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    fun motionTest(view: View) {
        Router.startUri(this, "/motionTest")
    }

    fun mvi(view: View) {
        Router.startUri(this, "/mvi")
    }

    fun leakMemory(view: View) {
        LeakMemoryActivity.dog?.call()
        Router.startUri(this, "/leakMemory")
    }
}