package com.example.plugin.base

/**
 * Created by chenyy on 2022/2/16.
 */

interface TransformCallBack {
    fun process(className: String, classBytes: ByteArray?): ByteArray?
    fun finish()
}