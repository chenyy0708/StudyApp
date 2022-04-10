package com.example.study.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import com.sankuai.waimai.router.annotation.RouterUri

/**
 * 描述:
 * 作者: ChenYy
 * 日期: 2022-04-10 16:56
 */
@RouterUri(path = ["/compose"])
class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello world!")
        }
    }
}