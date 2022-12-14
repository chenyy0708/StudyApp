package com.example.study.matrix

import android.content.Context
import com.example.study.logD
import com.tencent.matrix.plugin.DefaultPluginListener
import com.tencent.matrix.report.Issue
import com.tencent.matrix.util.MatrixLog

/**
 * Created by chenyy on 2022/5/26.
 */

class TestPluginListener(val context: Context) : DefaultPluginListener(context) {
    val TAG = "Matrix.TestPluginListener"

    override fun onReportIssue(issue: Issue?) {
        super.onReportIssue(issue)
        MatrixLog.e(TAG, issue.toString())
        logD(issue.toString(),tag = "MatrixStudy")
    }
}