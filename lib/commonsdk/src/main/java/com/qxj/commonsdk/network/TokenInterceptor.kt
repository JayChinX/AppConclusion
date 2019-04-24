package com.qxj.commonsdk.network

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset

abstract class TokenInterceptor(private var charset: Charset = UTF8) : Interceptor {

    private val TAG = TokenInterceptor::class.java.simpleName

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()

        //拿到正常接口请求的数据
        val tokenRequest = initToken(originRequest)

        //originResponse为请求回来的数据
        val originResponse = chain.proceed(tokenRequest)
        val isTokenInvalid = isTokenInvalid(originResponse, getResponseBodyString(originResponse))

        return if (isTokenInvalid) {
            synchronized(this) {
                GlobalScope.launch {
                    withContext(Dispatchers.Default) {
                        Log.d(TAG, "启动一个协程 去同步 token 接口")
                        retryToken()
                    }
                }

                val oldToken = originResponse.request().headers().get("Authenticator")

                val newResponse = chain.proceed(initToken(originRequest))
                val newToken = newResponse.request().headers().get("Authenticator")

                if (oldToken != newToken) {
                    Log.d(TAG, "是最新的token")
                }
                //重新请求本次接口
                newResponse
            }

        } else {
            originResponse
        }
    }

    private fun getResponseBodyString(response: Response?): String {
        if (response == null) {
            return ""
        }
        val responseBodyBuilder = StringBuilder()
        val responseBody = response.body()
        if (responseBody?.contentType() != null && responseBody.contentLength() > 0) {

            responseBody.use {
                val source = it.source()
                source.request(java.lang.Long.MAX_VALUE)
                val buffer = source.buffer

                responseBodyBuilder.append(buffer.clone().readString(charset))
            }

        }

        return responseBodyBuilder.toString()
    }

    /**
     * 为这个请求, 设置token信息
     */
    abstract fun initToken(originRequest: Request): Request

    abstract fun isTokenInvalid(response: Response, bodyString: String): Boolean

    abstract suspend fun retryToken()

}
