package com.qxj.commonsdk.dagger

import javax.inject.Inject

class Chef @Inject constructor(var menus: Menu): Cooking {

    override fun cook(): String {
        val menusMap = menus.menus
        val stringBuffer = StringBuffer()
        menusMap.forEach{
            if (it.value) stringBuffer.append(it.key)
        }
        return stringBuffer.toString()
    }
}