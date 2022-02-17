package com.example.plugin.base

/**
 * Created by chenyy on 2022/2/16.
 */

class DefaultClassNameFilter : ClassNameFilter {

    override fun filter(className: String): Boolean {
        whiteList.forEach {
            if (className.contains(it)) {
                return true
            }
        }
        return false
    }

    companion object {
        val whiteList = mutableListOf<String>().apply {
            add("kotlin")
            add("org")
            add("androidx")
            add("android")
        }
    }
}