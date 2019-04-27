package com.qxj.commonbase.coroutine

interface Callback <T> {

    fun onSuccess(value: T)

    fun onError(t: Throwable)
}