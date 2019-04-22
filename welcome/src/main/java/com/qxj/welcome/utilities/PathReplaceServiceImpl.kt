package com.qxj.welcome.utilities

import android.content.Context
import android.net.Uri
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PathReplaceService

/**
 * 路径替换服务
 */
@Route(path = "/home/service/PathReplaceServiceImpl")
class PathReplaceServiceImpl : PathReplaceService {

    private val TAG = PathReplaceServiceImpl::class.java.simpleName

    lateinit var mContext: Context

    //
    override fun forString(path: String): String {
        Log.d(TAG, "此 path = $path 是否需要替换 false")
        return path//处理后返回的path
    }

    override fun forUri(uri: Uri): Uri {
        Log.d(TAG, "此 uri = $uri 是否需要变换 false")
        return uri//
    }

    override fun init(context: Context) {
        this.mContext = context
        Log.d(TAG, "路径替换拦截器初始化")
    }
}