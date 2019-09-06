package com.hymnal.welcome.kodein

import com.hymnal.welcome.data.LiveDataBus
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class Presenter(val kodein: Kodein) {
    private val liveDataBus: LiveDataBus by kodein.instance()
}