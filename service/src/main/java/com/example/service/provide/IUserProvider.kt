package com.example.service.provide

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Created by chenyy on 2021/6/3.
 */

interface IUserProvider : IProvider {
    fun getUserInfo():String
}