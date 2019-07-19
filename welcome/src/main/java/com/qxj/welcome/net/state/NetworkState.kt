package com.qxj.welcome.net.state

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkInfo.DetailedState.*
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import com.qxj.welcome.base.AppWelcome

class NetworkState : LiveData<NetworkInfo>() {

    private val TAG = NetworkState::class.java.simpleName

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { NetworkState() }

        fun get() = instance
    }

    private val manager: ConnectivityManager = AppWelcome.INSTANCE
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallbackImpl = NetworkCallbackImpl(this, manager)


    override fun onActive() {
        super.onActive()
        registerNetworkCallback()
        postValue(manager.activeNetworkInfo)
    }

    @SuppressLint("NewApi")
    override fun onInactive() {
        super.onInactive()
        manager.unregisterNetworkCallback(networkCallbackImpl)
    }

    @SuppressLint("NewApi")
    private fun registerNetworkCallback() {
        val builder = NetworkRequest.Builder()
        val request = builder.build()
        manager.registerNetworkCallback(request, networkCallbackImpl)
    }

    public override fun postValue(value: NetworkInfo?) {
        super.postValue(value)
    }

    public override fun setValue(value: NetworkInfo?) {
        valueChange(value)
        super.setValue(value)
    }

    private fun valueChange(value: NetworkInfo?) {
        when (value?.detailedState) {
            IDLE -> {//空闲
                Log.d(TAG, "网络空闲")
            }
            SCANNING -> {//正在扫描
                Log.d(TAG, "正在扫描网络...")
            }
            CONNECTING -> {//连接中  android 系统把CONNECTING，AUTHENTICATING，OBTAINING_IPADDR都规为CONNECTING
                Log.d(TAG, "网络连接中...")
            }
            AUTHENTICATING -> {//正在进行身份验证...

            }
            OBTAINING_IPADDR -> {//正在获取Ip地址

            }
            CONNECTED -> {//已连接
                Log.d(TAG, "网络已连接")
            }
            SUSPENDED -> {//已暂停
                Log.d(TAG, "网络已暂停")
            }
            DISCONNECTING -> {//正在断开连接...
                Log.d(TAG, "网络连接正在断开...")
            }
            DISCONNECTED -> {//已断开
                Log.d(TAG, "网络已断开")
            }
            FAILED -> {//尝试连接失败
                Log.d(TAG, "网络连接失败")
            }
            BLOCKED -> {//已阻止
                Log.d(TAG, "网络连接已阻止")
            }
            VERIFYING_POOR_LINK -> {//暂时关闭（网络状况不佳）
                Log.d(TAG, "网络状况不佳,暂时关闭")
            }
            CAPTIVE_PORTAL_CHECK -> {//判断是否需要浏览器二次登录

            }
            null -> Log.d(TAG, "网络已断开")
        }
    }
}