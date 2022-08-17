package com.example.study.matrix

import com.tencent.mrs.plugin.IDynamicConfig

/**
 * Created by chenyy on 2022/5/26.
 */

class DynamicConfigImplDemo : IDynamicConfig {
    fun isFPSEnable(): Boolean {
        return true
    }

    fun isTraceEnable(): Boolean {
        return true
    }

    fun isMatrixEnable(): Boolean {
        return true
    }

    fun isDumpHprof(): Boolean {
        return false
    }

    override fun get(key: String?, defStr: String?): String {
        return ""
    }

    override fun get(key: String?, defInt: Int): Int {
        return 0
    }

    override fun get(key: String?, defLong: Long): Long {
        if (IDynamicConfig.ExptEnum.clicfg_matrix_fps_dropped_normal.name.equals(key)) {
            return 10000L;
        }
        return defLong
    }

    override fun get(key: String?, defBool: Boolean): Boolean {
        return true
    }

    override fun get(key: String?, defFloat: Float): Float {
        return 0f
    }
}