package com.hymnal.base.network

import com.hymnal.base.CommonBaseApp
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarImpl(private val cookieStore: CookieStore = PersistentCookieStore(CommonBaseApp.INSTANCE)) : CookieJar {

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        if (cookies.isNotEmpty()) cookieStore.add(url, cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore.get(url)
    }
}