package com.hymnal.welcome.koin

import org.koin.dsl.module

val appModule = module {
    single<UserService> { UserServiceImpl() }
    factory { ServiceManager(get()) }
}