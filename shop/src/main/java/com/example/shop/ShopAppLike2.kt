package com.example.shop
import android.util.Log
import com.example.modulelike.core.BaseAppLike
import com.study.interfaces.annotation.ModuleLike

/**
 * Created by chenyy on 2021/6/22.
 */

@ModuleLike
class ShopAppLike2 : BaseAppLike() {

    override fun onCreate() {
        Log.d("AppLike", "ShopAppLike2初始化")
    }
}