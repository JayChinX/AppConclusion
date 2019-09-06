package com.hymnal.welcome.debug

import android.content.Context
import android.util.Log
import com.hymnal.base.BaseApplication
import com.hymnal.base.CommonBaseApp
import com.hymnal.welcome.base.App
import com.hymnal.welcome.kodein.httpClientModule
import com.hymnal.welcome.kodein.serviceModule
import com.hymnal.welcome.koin.appModule
import com.hymnal.welcome.koin.sampleViewModel
import com.hymnal.welcome.utilities.initARouter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.android.support.androidSupportModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : BaseApplication(), KodeinAware {

    private val TAG = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        initARouter()
        Log.d(TAG, "ARouter init")

        /**
         * koin
         */
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, sampleViewModel)
        }
    }

    override fun registerModule(): List<String> {
        return arrayListOf(
            App::class.java.name,
                CommonBaseApp::class.java.name)
    }

    /**
     * Kodein
     */
    override val kodein: Kodein = Kodein.lazy {

        bind<Context>() with singleton { this@App }
        import(androidModule(this@App))
        import(androidSupportModule(this@App))

        import(serviceModule)
        import(httpClientModule)
    }
}