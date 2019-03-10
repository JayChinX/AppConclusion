package com.qxj.commonsdk.network

import com.google.gson.Gson
import okhttp3.RequestBody

object GsonUtils {

    fun toRequestBody(map: HashMap<String, String>): RequestBody = Gson().toRequestBody(map)
}