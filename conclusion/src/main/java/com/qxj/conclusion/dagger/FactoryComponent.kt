package com.qxj.conclusion.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CookModules::class])
interface FactoryComponent {
    fun inject(activity: FactoryActivity)
}