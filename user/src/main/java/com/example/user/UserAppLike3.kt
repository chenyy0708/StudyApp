package com.example.user
import android.util.Log
import com.example.modulelike.core.BaseAppLike
import com.study.interfaces.annotation.ModuleLike

/**
 * Created by chenyy on 2021/6/22.
 */

@ModuleLike
class UserAppLike3 : BaseAppLike() {

    override fun onCreate() {
        Log.d("AppLike", "UserAppLike3初始化")
    }
}