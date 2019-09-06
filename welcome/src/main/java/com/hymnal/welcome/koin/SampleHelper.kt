package com.hymnal.welcome.koin

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val sampleViewModel = module {
    viewModel { SampleViewModel() }
}