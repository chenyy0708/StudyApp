package com.example.study.mvi

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.study.databinding.ActivityMviBinding
import com.sankuai.waimai.router.annotation.RouterUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by chenyy on 2022/7/18.
 */

@RouterUri(path = ["/mvi"])
@AndroidEntryPoint
class MVIActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMviBinding.inflate(layoutInflater).apply {
            setContentView(root)
            binding = this
        }
        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetDataEvents)
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.dataState.collect { dataState ->
                when (dataState) {
                    is DataState.Success<List<String>> -> {
                        binding.tvTest.text = dataState.data.toString()
                    }
                    is DataState.Loading -> {
                        binding.tvTest.text = "加载中"
                    }
                    is DataState.Error -> {
                        binding.tvTest.text = dataState.exception.message ?: ""
                    }
                }
            }
        }
    }
}