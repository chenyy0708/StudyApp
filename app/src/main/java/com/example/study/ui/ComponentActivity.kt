package com.example.study.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.example.study.R

/**
 * Created by chenyy on 2021/6/3.
 */

class ComponentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component)
    }

    fun user(view: View) {
        ARouter.getInstance().build("/user/activity").navigation()
    }

    fun shop(view: View) {
        ARouter.getInstance().build("/shop/activity").navigation()
    }

    fun home(view: View) {
        ARouter.getInstance().build("/home/activity").navigation()
    }

}