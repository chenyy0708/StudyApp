package com.example.study.mvi


/**
 * Created by chenyy on 2022/7/18.
 */

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Throwable) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}