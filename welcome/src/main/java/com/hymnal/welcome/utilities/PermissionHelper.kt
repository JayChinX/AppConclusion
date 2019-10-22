package com.hymnal.welcome.utilities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hymnal.alert.Alert
import com.hymnal.alert.alert


fun AppCompatActivity.checkPermission(run: (() -> Unit)? = null) {
    checkPermission { it ->

        if (it) {
            //有权限
            run?.invoke()
        } else {
            //没有权限
            alert {
                title = "权限说明"
                msg = "该应用需要访问存储权限，现在去设置"
                left = "取消"
                right = "去设置"
                type = Alert.Type.NORMAL
                cancelable = false
                listener = {
                    if (it) {
                        //点击确认去设置
                        toSettings()
                    } else {
                        //点击取消
                    }
                }
            }

        }
    }
}

private fun AppCompatActivity.checkPermission(hasDo: (Boolean) -> Unit) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        permission.init(this)
        permission.checkStoragePermission(Runnable {
            hasDo(true)
        }, Runnable {
            hasDo(false)
        })
    } else {
        hasDo(true)
    }

}

fun AppCompatActivity.toSettings() {
    val intent = android.content.Intent()
    intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = android.net.Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, Permission.REQUEST_CODE_PERMISSION)

}

private val AppCompatActivity.permission by lazy { Permission() }

fun AppCompatActivity.permissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    permission.onRequestPermissionsResult(requestCode, permissions, grantResults)
}

fun AppCompatActivity.settingsResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == Permission.REQUEST_CODE_PERMISSION) checkPermission()
}

class Permission {
    private var mHasPermissionRunnable: Runnable? = null
    private var mNoPermissionRunnable: Runnable? = null

    companion object {
        const val REQUEST_CODE_PERMISSION = 777
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
        val permission = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
        checkPermission(permission, hasPermissionDo, noPermissionDo)
    }

    fun checkCameraPermission(hasPermissionDo: Runnable, noPermissionDo: Runnable) {
        val permission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        checkPermission(permission, hasPermissionDo, noPermissionDo)
    }

    private fun checkPermission(
        permissions: Array<out String>,
        hasPermissionDo: Runnable,
        noPermissionDo: Runnable
    ) {
        mHasPermissionRunnable = null
        mNoPermissionRunnable = null
        when {
            isPermissionsGranted(permissions) -> hasPermissionDo.run()
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                permissions[0]
            ) -> noPermissionDo.run()
            else -> {
                mHasPermissionRunnable = hasPermissionDo
                mNoPermissionRunnable = noPermissionDo
                ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_CODE_PERMISSION)
            }
        }
    }


    private fun isPermissionsGranted(permissions: Array<out String>): Boolean {
        for (it in permissions) {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            )
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

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (isAllGranted(grantResults)) mHasPermissionRunnable?.run()
            else mNoPermissionRunnable?.run()
        }
    }

}