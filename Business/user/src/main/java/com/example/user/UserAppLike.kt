package com.example.user
import android.util.Log
import com.example.modulelike.core.BaseAppLike
import com.study.interfaces.annotation.ModuleLike

/**
 * Created by chenyy on 2021/6/22.
 */

@ModuleLike
class UserAppLike : BaseAppLike() {

    override fun onCreate() {
        Log.d("AppLike", "UserAppLike初始化")
    }
}