package com.example.user
import android.util.Log
import com.example.common.BaseAppLike
import com.example.common.annotation.ModuleLike

/**
 * Created by chenyy on 2021/6/22.
 */

@ModuleLike
class UserAppLike : BaseAppLike() {

    override fun onCreate() {
        Log.d("AppLike", "UserAppLike初始化")
    }
}