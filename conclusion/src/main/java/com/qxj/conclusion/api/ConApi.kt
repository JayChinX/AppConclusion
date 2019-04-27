package com.qxj.conclusion.api

import com.qxj.commonbase.network.Api
import com.qxj.commonbase.network.Result
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ConApi : Api {

    /**
     * post
     */
    //指定一个对象作为http请求体
    @Headers("Content-Type: application/json")
    @POST("UserNowLocation") //添加Body请求体
    fun <T> getUserLocation(@Body body: RequestBody): Observable<Result<List<T>>>

    //    @Headers("apikey:81bf9da930c7f9825a3c3383f1d8d766")
    @FormUrlEncoded//post 上传表单
    @POST("user")
    fun <T> login(@Field("first_name") first: String, @Field("last_name") last: String): Observable<Result<T>>
}