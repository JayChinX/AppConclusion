package com.hymnal.welcome.kodein

import com.google.gson.Gson
import com.hymnal.base.network.CoroutineCallAdapterFactory
import com.hymnal.welcome.data.LiveDataBus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val liveDataBusModule = Kodein {
    bind<LiveDataBus>() with singleton { LiveDataBus.get() }
}

const val HTTP_CLIENT_MODULE_TAG = "httpClientModule"
const val TIME_OUT_SECONDS = 20
val httpClientModule = Kodein.Module(HTTP_CLIENT_MODULE_TAG) {
    bind<Retrofit.Builder>() with singleton { Retrofit.Builder() }

    bind<OkHttpClient.Builder>() with singleton { OkHttpClient.Builder() }

    bind<Retrofit>() with singleton {
        instance<Retrofit.Builder>()//委托给 bind<Retrofit.Builder>() 函数
            .baseUrl("")
            .client(instance())//委托给 bind<OkHttpClient>() 函数
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    bind<OkHttpClient>() with singleton {
        instance<OkHttpClient.Builder>()//委托给 bind<OkHttpClient.Builder>() 函数
            .connectTimeout(
                TIME_OUT_SECONDS.toLong(),
                TimeUnit.SECONDS
            )
            .readTimeout(
                TIME_OUT_SECONDS.toLong(),
                TimeUnit.SECONDS
            )
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }

    bind<Gson>() with singleton { Gson() }
}

const val SERVICE_MODULE_TAG = "serviceModule"

val serviceModule = Kodein.Module(SERVICE_MODULE_TAG) {

    bind<UserService>() with singleton {
        //Retrofit 的对象获取已经在httpClientModule中声明
        instance<Retrofit>().create(UserService::class.java)
    }

    bind<ServiceManager>() with singleton {
        ServiceManager(instance()) //userService 的获取方式已经声明
    }
}