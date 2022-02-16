package com.example.shop
import android.util.Log
import com.example.common.BaseAppLike
import com.example.common.annotation.ModuleLike

/**
 * Created by chenyy on 2021/6/22.
 */

@ModuleLike
class ShopAppLike4 : BaseAppLike() {

    override fun onCreate() {
        Log.d("AppLike", "ShopAppLike4初始化")
    }
}