package com.qxj.commonbase.network

import com.google.gson.Gson
import okhttp3.RequestBody
import java.util.HashMap

//region json字符串转换
fun Gson.toRequestBody(map: HashMap<String, String>): RequestBody =
        RequestBody.create(okhttp3.MediaType.parse("Content-Type:application/json"), Gson().toJson(map))

//endregion