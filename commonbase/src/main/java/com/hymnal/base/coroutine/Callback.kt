package com.hymnal.base.coroutine

interface Callback <T> {

    fun onSuccess(value: T)

    fun onError(t: Throwable)
}