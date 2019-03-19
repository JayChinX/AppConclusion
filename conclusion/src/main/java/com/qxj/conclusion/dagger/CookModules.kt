package com.qxj.conclusion.dagger

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CookModules {
    @Singleton
    @Provides
    fun providerMenus(): Map<String, Boolean> {
        val menus = LinkedHashMap<String, Boolean>()
        menus["酸菜鱼"] = false
        menus["烤羊腿"] = true
        menus["干锅牛肉"] = true
        menus["汤"] = true
        return menus
    }
}