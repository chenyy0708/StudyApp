package com.example.study

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 * Created by chenyy on 2021/5/28.
 */

class MVM : ViewModel() {

    fun test() {
        viewModelScope.launch {

        }

        liveData<Int> {

        }
    }
}