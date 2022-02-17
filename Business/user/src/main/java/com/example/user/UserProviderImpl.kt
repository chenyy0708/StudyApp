package com.example.user

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.service.provide.IUserProvider

/**
 * Created by chenyy on 2021/6/3.
 */

@Route(path = "/user/provider")
class UserProviderImpl : IUserProvider {
    override fun getUserInfo(): String = "HaHaHa"

    override fun init(context: Context?) {
    }
}