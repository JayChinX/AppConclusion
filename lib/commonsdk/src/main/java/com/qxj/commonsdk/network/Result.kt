package com.qxj.commonsdk.network


/**
 * 通用返回参数
 */
//data class Result<T>(var ecdoe: Int, var message: String, var data: T?)

data class Result<T>(var ecdoe: Int, var message: T?)
