package com.qxj.conclusion.MVPDevelop.Model

object UserUtils {
    //公共方法
    fun subString(str: String, left: Int, right: Int) : String {
        return when {
            left < right -> str.substring(left, right)
            left == right -> str[left].toString() // str.get(left).toString()
            else -> ""
        }
    }

    fun splitString(str: String, char: String) : String {
        var a = ""
        str.split(".").forEach { a = "$it" }
        return a
    }
}