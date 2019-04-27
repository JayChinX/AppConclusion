package com.qxj.commonbase.network

/**
 * 通用返回参数
 */
data class Result<T>(var ecdoe: Int, var message: String, var data: T?)
