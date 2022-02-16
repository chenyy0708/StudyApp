package com.example.plugin.base

/**
 * Created by chenyy on 2022/2/16.
 */

interface ClassNameFilter {
    fun filter(className: String): Boolean
}