package com.example.modulelike.core

import com.example.modulelike.utils.ReflectUtils

/**
 * Created by chenyy on 2022/2/18.
 */

object ModuleProvider {
    private val moduleLikes = mutableMapOf<String, BaseAppLike?>()

    fun register(clazz: Class<*>) {
        moduleLikes[clazz.name] = loadModuleLike(clazz)
    }

    fun init() {
        try {
            val clazz = Class.forName(Const.MODULE_LOADER_INIT)
            ReflectUtils.invokeStaticMethod<Any>(clazz, Const.INIT_METHOD)
            moduleLikes.forEach {
                it.value?.onCreate()
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun loadModuleLike(clazz: Class<*>): BaseAppLike? {
        try {
            return clazz.newInstance() as BaseAppLike
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}