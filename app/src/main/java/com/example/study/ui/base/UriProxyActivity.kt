package com.example.study.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sankuai.waimai.router.common.DefaultUriRequest
import com.sankuai.waimai.router.core.OnCompleteListener
import com.sankuai.waimai.router.core.UriRequest


/**
 * Created by chenyy on 2022/2/9.
 */

class UriProxyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DefaultUriRequest.startFromProxyActivity(this, object : OnCompleteListener {
            override fun onSuccess(request: UriRequest) {
                finish()
            }

            override fun onError(request: UriRequest, resultCode: Int) {
                finish()
            }
        })
    }
}