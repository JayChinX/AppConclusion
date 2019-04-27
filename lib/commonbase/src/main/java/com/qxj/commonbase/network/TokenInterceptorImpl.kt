package com.qxj.commonbase.network

import android.util.Log
import com.google.gson.Gson
import okhttp3.Request
import okhttp3.Response

class TokenInterceptorImpl : TokenInterceptor() {

    private val TAG = TokenInterceptor::class.java.simpleName
    private val gson = Gson()
    override fun initToken(originRequest: Request): Request =
            originRequest.newBuilder()
                    .addHeader("token-header", "token value")
                    .build()

    override fun isTokenInvalid(response: Response, bodyString: String): Boolean {
        val error = gson.fromJson(bodyString, Result::class.java)
        Log.d(TAG, "获取错误码 $error")
        return error.ecdoe == 401
    }

    override suspend fun retryToken() {
        //重新请求token
        Log.d(TAG, "重新同步 token")
    }
}