package com.example.study

/**
 * Created by chenyy on 2021/6/10.
 */

data class CTPayLocation(
        var longitude: Double = 0.0,
        var latitude: Double = 0.0
) {
    override fun toString(): String {
        return "CTPayLocation(longitude=$longitude, latitude=$latitude)"
    }
}