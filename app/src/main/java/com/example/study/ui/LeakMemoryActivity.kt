package com.example.study.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.study.databinding.ActivityLeakMemoryBinding
import com.example.study.utils.SingleTest
import com.sankuai.waimai.router.annotation.RouterUri

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
        super.onDestroy()
    }

    fun singleton(view: View) {
        SingleTest.getInstance().init(this)
        SingleTest.getInstance().showToast("单例泄露")
    }
}
