package com.hymnal.welcome.ui.home.data

import com.hymnal.base.coroutine.Callback
import com.hymnal.base.mvvm.Repository
import com.hymnal.welcome.net.api.ApiService
import com.hymnal.welcome.proxy.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BlankRepository : Repository {

    suspend fun getUserName() = suspendCoroutine<String> {
        getUser(object : Callback<String> {
            override fun onSuccess(value: String) {
                it.resume(value)
            }

            override fun onError(t: Throwable) {
                it.resumeWithException(t)
            }

        })
    }

    suspend fun getStudents() =
            withContext(Dispatchers.Default) {
                try {
                    val students = ApiService.getStudents<Student>().await().data
                    val studnet1 = ApiService.getStudents<Student>().await().data
                    val result = mutableListOf<Student>()
                            .apply {
                                addAll(students!!)
                                addAll(studnet1!!)
                            }

                    result
                } catch (e: Throwable) {
                    e.printStackTrace()
                    throw e
                }

            }

    private var count = 0
    private fun getUser(callback: Callback<String>) {
        count++
        if (count < 4) {
            callback.onSuccess("�ɹ� $count")
        } else {
            callback.onError(Exception("������� $count"))
        }
    }
}