package com.qxj.conclusion.ConclusionUtils

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.checkPermission
import android.view.View
import com.qxj.conclusion.AppConclusionActivity
import com.qxj.conclusion.R

class PermissionUtil(private val context: AppConclusionActivity) {
    private var mHasPermissionRunnable: Runnable? = null
    private var mNoPermissionRunnable: Runnable? = null
    private var REQUEST_CODE_PERMISSION = 1000

    private val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    fun checkStoragePermission(hasPermissionDo: Runnable) {
        var permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        checkPermission(permission, hasPermissionDo, Runnable {
            context.showPermissionDialog("不开启存储权限，无法访问相册哦~")
        })
    }

    fun checkCameraPermission(hasPermissionDo: Runnable) {
        var permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        checkPermission(permission, hasPermissionDo, Runnable {
            context.showPermissionDialog("不开启相机权限，无法拍照哦~")
        })
    }

    private fun checkPermission(permissions: Array<out String>, hasPermissionDo: Runnable, noPermissionDo: Runnable) {
        mHasPermissionRunnable = null
        mNoPermissionRunnable = null
        if (isPermissionsGranted(permissions)) hasPermissionDo.run()
        else if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissions.get(0))) {
            noPermissionDo.run()
        } else {
            mHasPermissionRunnable = hasPermissionDo
            mNoPermissionRunnable = noPermissionDo
            ActivityCompat.requestPermissions(context, permissions, REQUEST_CODE_PERMISSION)
        }
    }


    private fun isPermissionsGranted(permissions: Array<out String>): Boolean {
        for (it in permissions) {
            if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
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

    private fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (isAllGranted(grantResults))
                mHasPermissionRunnable?.run()
            else mNoPermissionRunnable?.run()
        }
    }


}