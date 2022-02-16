package com.example.plugin.base.ext

import com.example.plugin.base.utils.Log
import org.apache.commons.io.FileUtils
import java.io.File

/**
 * Created by chenyy on 2022/2/16.
 */

fun copyIfLegal(srcFile: File?, destFile: File) {
    if (srcFile?.name?.contains("module-info") != true) {
        try {
            srcFile?.apply {
                FileUtils.copyFile(srcFile, destFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        Log.info("copyIfLegal module-info:" + srcFile.name)
    }
}

