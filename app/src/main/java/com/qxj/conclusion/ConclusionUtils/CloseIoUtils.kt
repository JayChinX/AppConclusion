package com.qxj.conclusion.ConclusionUtils

import java.io.FileInputStream
import java.io.FileOutputStream

object CloseIoUtils {

    fun closeIO(fos: FileOutputStream, fis: FileInputStream) {

        fos.close()
        fis.close()
    }
    fun closeIO(fis: FileInputStream) {

        fis.close()
    }
    fun closeIO(fos: FileOutputStream) {

        fos.close()
    }
}