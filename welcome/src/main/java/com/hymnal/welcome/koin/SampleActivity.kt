package com.hymnal.welcome.koin

import com.hymnal.welcome.R
import com.hymnal.welcome.base.BaseActivity
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SampleActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_sample


    private val serviceManager: ServiceManager by inject()


    private val sampleViewModel: SampleViewModel by viewModel()

    override fun subscribeUi() {

        val service = serviceManager.userService

        val serviceManager2: ServiceManager = get()
        val service2 = serviceManager2.userService
    }


}
