package com.example.plugin.base

/**
 * Created by chenyy on 2022/2/16.
 */

interface DeleteCallBack {
    fun delete(className: String, classBytes: ByteArray)
}