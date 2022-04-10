package com.example.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.study.hilt.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by chenyy on 2021/5/28.
 */
@HiltViewModel
class MVM @Inject constructor(handle: SavedStateHandle?, repository: MovieRepository?) :
    ViewModel() {

    fun test() {
        viewModelScope.launch {

        }

        liveData<Int> {

        }
    }
}