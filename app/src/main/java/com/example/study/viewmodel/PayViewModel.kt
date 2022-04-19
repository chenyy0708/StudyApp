package com.example.study.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.study.ui.PayActivity

/**
 * Created by chenyy on 2022/4/19.
 */

class PayViewModel : ViewModel() {
    private var _selfPayTypes = mutableStateListOf<PayActivity.SelfPayModel>()
    private lateinit var _selectedPayType: MutableState<PayActivity.PayTypeModel>
    val selfPayTypes: MutableList<PayActivity.SelfPayModel> = _selfPayTypes

    val selectedPayType by lazy {
        _selectedPayType
    }

    fun getSelfPayTypeModels() {
        _selfPayTypes.addAll(
            listOf(
                PayActivity.SelfPayModel(
                    title = "交通银行 储蓄卡 (2222)",
                    discount = "立减1-10元", cardInfoId = "2222"
                ), PayActivity.SelfPayModel(
                    title = "招商银行 储蓄卡 (6680)",
                    discount = "立减1-30元", cardInfoId = "6680"
                )
            )
        )
        _selectedPayType = mutableStateOf(selfPayTypes[0])
    }

    fun selectPayType(payTypeModel: PayActivity.PayTypeModel) {
        _selectedPayType.value = payTypeModel
    }
}