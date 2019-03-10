package com.qxj.commonsdk.network

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface Api {

    companion object {
        val HOST = "http://10.206.24.100:9911/location/"
    }
    /**
     * get
     */
    @GET("User")//  User?userId="..."
    fun <T> addUser(@Query("userId") userId: String): Observable<Result<T>>

    @GET("changePwd")//  changePwd?{..., ...}
    fun <T> toChangePassword(@QueryMap options: Map<String, String>): Observable<Result<T>>

    @GET("group/{id}/users")//  group/1/users?sort="2"
    fun <T> getGroup(@Path("id") group: Int, @Query("userId") userId: String): Observable<Result<T>>

    /**
     * post
     */
    //指定一个对象作为http请求体
    @Headers("Content-Type: application/json")
    @POST("UserNowLocation")//添加Body请求体
    fun <T> getUserLocation(@Body body: RequestBody): Observable<Result<List<T>>>

    //    @Headers("apikey:81bf9da930c7f9825a3c3383f1d8d766")
    @FormUrlEncoded//post 上传表单
    @POST("user")
    fun <T> login(@Field("first_name") first: String, @Field("last_name") last: String): Observable<Result<T>>

    //Header头部
    @Headers("Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App")
    @GET("user")
    fun <T> getUser(): Observable<Result<T>>

    @GET("")
    fun <T> getPassword(@Header("Author") header: String): Observable<Result<T>>

    /**
     * 下载
     */

    /**
     * 断点续传下载接口
     */
    @Streaming//大文件需要加入这个判断，防止下载过程中写入到内存中
    @GET
    fun download(@Header("RANGE") start: String, @Url url: String): Observable<ResponseBody>

    /**
     * 实例代码
     */

//ApiService.getUserLocation("H2409761")
//                .async(100)//扩展函数
//                .doOnSubscribe {
//                    //开始网络请求 showLoading()
//                }
//                .doAfterTerminate {
//                    //网络请求完毕 hideLoading()
//                }
//                .bindLifecycle(owner)//扩展函数绑定生命周期
//                .subscribe({ data ->
//                    data?.let {
//                        it.data.let {
//
//                        }
//                    }
//                }, {
//                    e ->
//
//                })
//        ApiService.login(name, password)
//                .async(100)//扩展函数
//                .doOnSubscribe {
//                    //开始网络请求
//                    mView.get()?.showLoading()
//                }
//                .doAfterTerminate {
//                    //网络请求完毕
//                    mView.get()?.hideLoading()
//                }
//                .bindLifecycle(owner)//扩展函数绑定生命周期
//                .subscribe(object : Response<UserBean>() {
//                    override fun success(data: UserBean) {
//                    }
//
//                    override fun failure(statusCode: Int) {
//                    }
//                })//自定义范围数据拦截器
}