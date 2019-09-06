package com.hymnal.welcome.kodein

import org.kodein.di.Kodein
import org.kodein.di.android.AndroidComponentsWeakScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

const val SAMPLE_MODULE_TAG = "sampleModule"

val sampleKodeinModel = Kodein.Module(SAMPLE_MODULE_TAG) {

    bind<SampleViewModel>() with scoped(AndroidComponentsWeakScope).singleton {
        //一些对象的实例化需要Context的上下文对象，我们通过bind<MainActivity>() with instance(this@MainActivity)完成MainActivity的绑定
        instance<SampleActivity>().viewModel(SampleViewModel::class.java)
    }
}