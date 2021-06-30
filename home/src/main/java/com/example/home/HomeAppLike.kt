package com.example.home
import android.util.Log
import com.example.common.BaseAppLike
import com.example.common.annotation.ModuleLike

/**
 * Created by chenyy on 2021/6/22.
 */

@ModuleLike
class HomeAppLike : BaseAppLike() {

    override fun onCreate() {
        Log.d("AppLike", "HomeAppLike初始化")
    }
}