package com.example.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.annotation.ModuleLike

/**
 * Created by chenyy on 2021/6/3.
 */

@Route(path = "/shop/activity")
class ShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
    }
}