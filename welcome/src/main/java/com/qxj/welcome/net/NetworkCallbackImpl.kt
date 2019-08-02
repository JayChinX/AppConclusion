package com.qxj.welcome.net.state

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities

@SuppressLint("NewApi")
class NetworkCallbackImpl constructor(private val networkLiveData: NetworkState,
                                      private val manager: ConnectivityManager)
    : ConnectivityManager.NetworkCallback() {

    private val TAG = NetworkCallbackImpl::class.java.simpleName
    //网络可用的回调连接成功
    override fun onAvailable(network: Network?) {
        super.onAvailable(network)
        networkLiveData.postValue(manager.activeNetworkInfo)
//        Log.d(TAG, "连接成功")
    }

    //实践中在网络连接正常的情况下，丢失数据会有回调
    override fun onLosing(network: Network?, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
//        networkLiveData.postValue(manager.activeNetworkInfo)
    }

    // 网络不可用时调用和onAvailable成对出现
    override fun onLost(network: Network?) {
        super.onLost(network)
        networkLiveData.postValue(manager.activeNetworkInfo)
//        Log.d(TAG, "连接断开")
    }

    /**
     * 字面直接能理解
     * @param network 新连接网络
     * @param networkCapabilities 新连接网络的一些能力参数
     */
    override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
        super.onCapabilitiesChanged(network, networkCapabilities)
//        networkLiveData.postValue(manager.activeNetworkInfo)
    }

    //网络连接属性改变
    override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
        super.onLinkPropertiesChanged(network, linkProperties)
//        networkLiveData.postValue(manager.activeNetworkInfo)
    }

    //网络缺失network
    override fun onUnavailable() {
        super.onUnavailable()
//        networkLiveData.postValue(manager.activeNetworkInfo)
    }
}