package com.example.study.mvi

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by chenyy on 2022/7/18.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository?,
    @Assisted handle: SavedStateHandle?,
) : ViewModel() {

    private val _dataState = MutableStateFlow<DataState<List<String>>>(DataState.Loading)
    val dataState
        get() = _dataState.asStateFlow()

    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetDataEvents -> {
                    mainRepository?.getData()?.onEach { dataState ->
                        _dataState.value = dataState
                    }?.launchIn(viewModelScope)
                }
                is MainStateEvent.None -> {

                }
            }
        }
    }
}


sealed class MainStateEvent {
    object GetDataEvents : MainStateEvent()
    object None : MainStateEvent()
}

