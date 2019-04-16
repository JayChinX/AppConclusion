package com.qxj.welcome.utils

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

@Route(path = "/home/service/json")
class JsonServiceImpl : SerializationService {
    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        return JSON.parseObject(input, clazz)
    }

    override fun init(context: Context?) {

    }

    override fun object2Json(instance: Any?): String {
        return JSON.toJSONString(instance)
    }

    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        return JSON.parseObject(input, clazz)
    }

}