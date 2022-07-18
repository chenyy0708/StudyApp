package com.example.study.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * Created by chenyy on 2022/7/18.
 */

class MainRepository @Inject constructor() {

    suspend fun getData(): Flow<DataState<List<String>>> {
        return flow {
            emit(DataState.Loading)
            kotlinx.coroutines.delay(3 * 1000L)
            emit(DataState.Success(listOf("图片1", "图片2", "图片3")))
        }.catch {
            emit(DataState.Error(it))
        }
    }

}