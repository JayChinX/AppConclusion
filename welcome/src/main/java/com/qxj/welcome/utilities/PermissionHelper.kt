package com.qxj.welcome.utilities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun AppCompatActivity.checkPermission(hasDo: (Boolean) -> Unit) {
    permission.init(this)
    permission.checkStoragePermission(Runnable {
        hasDo(true)
    }, Runnable {
        hasDo(false)
    })
}

private val AppCompatActivity.permission by lazy { Permission() }

fun AppCompatActivity.permissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    permission.onRequestPermissionsResult(requestCode, permissions, grantResults)
}

class Permission {
    private var mHasPermissionRunnable: Runnable? = null
    private var mNoPermissionRunnable: Runnable? = null

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1000
    }

    private var activity: AppCompatActivity? = null

    fun init(activity: AppCompatActivity) {
        this.activity = activity
    }


    /**
     * @param hasPermissionDo 有权限
     * @param noPermissionDo 没有权限
     */
    fun checkStoragePermission(hasPermissionDo: Runnable, noPermissionDo: Runnable) {
        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        checkPermission(permission, hasPermissionDo, noPermissionDo)
    }

    fun checkCameraPermission(hasPermissionDo: Runnable, noPermissionDo: Runnable) {
        val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        checkPermission(permission, hasPermissionDo, noPermissionDo)
    }

    private fun checkPermission(permissions: Array<out String>, hasPermissionDo: Runnable, noPermissionDo: Runnable) {
        mHasPermissionRunnable = null
        mNoPermissionRunnable = null
        when {
            isPermissionsGranted(permissions) -> hasPermissionDo.run()
            ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissions[0]) -> noPermissionDo.run()
            else -> {
                mHasPermissionRunnable = hasPermissionDo
                mNoPermissionRunnable = noPermissionDo
                ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_CODE_PERMISSION)
            }
        }
    }


    private fun isPermissionsGranted(permissions: Array<out String>): Boolean {
        for (it in permissions) {
            if (ContextCompat.checkSelfPermission(activity!!, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    private fun isAllGranted(grantResults: IntArray): Boolean {
        for (it in grantResults) {
            if (it != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (isAllGranted(grantResults)) mHasPermissionRunnable?.run()
            else mNoPermissionRunnable?.run()
        }
    }


}