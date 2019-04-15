package com.qxj.multichannel

import com.qxj.commondata.paging.Student
import com.qxj.commondata.room.User
import java.time.LocalDate

object DataUtils {

    fun startDate() {
        val today = LocalDate.now()
        println("time is $today")

        val (id, name) = Student(0, "qxj")
        println("student: id = $id, name = $name")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        startDate()
    }

}