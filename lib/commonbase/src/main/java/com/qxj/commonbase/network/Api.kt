package com.qxj.commonbase.network

import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    companion object {
        val HOST = "http://10.206.24.100:9911/location/"
    }
    /**
     * get
     */
    @GET("User")//  User?userId="..."
    fun <T> addUser(@Query("userId") userId: String): Deferred<Result<T>>

    @GET("changePwd")//  changePwd?{..., ...}
    fun <T> toChangePassword(@QueryMap options: Map<String, String>): Deferred<Result<T>>

    @GET("group/{id}/users")//  group/1/users?sort="2"
    fun <T> getGroup(@Path("id") group: Int, @Query("userId") userId: String): Deferred<Result<T>>

    /**
     * post
     */
    //指定一个对象作为http请求体
    @Headers("Content-Type: application/json")
    @POST("UserNowLocation")//添加Body请求体
    fun <T> getUser(@Body body: RequestBody): Deferred<Result<List<T>>>

    //    @Headers("apikey:81bf9da930c7f9825a3c3383f1d8d766")
    @FormUrlEncoded//post 上传表单
    @POST("user")
    fun <T> logins(@Field("first_name") first: String, @Field("last_name") last: String): Deferred<Result<T>>

    //Header头部
    @Headers("Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App")
    @GET("user")
    fun <T> getUser(): Deferred<Result<T>>

    @GET("")
    fun <T> getStudent(): Call<Result<T>>

    fun <T> getStudents(): Deferred<Result<List<T>>>

    @GET("")
    fun <T> getPassword(@Header("Author") header: String): Deferred<Result<T>>

    /**
     * 下载
     */

    /**
     * 断点续传下载接口
     */
    @Streaming//大文件需要加入这个判断，防止下载过程中写入到内存中
    @GET
    fun download(@Header("RANGE") start: String, @Url url: String): Deferred<ResponseBody>
}