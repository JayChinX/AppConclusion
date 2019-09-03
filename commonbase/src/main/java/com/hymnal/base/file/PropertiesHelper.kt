package com.hymnal.base.file

import java.io.FileReader
import java.util.*

//run返回最后一行  apply返回对象本身
//obj.let 内部it指代对象   with(obj)传入对象，内部this指代传入对象

@Throws
fun Properties.getValue(key: String, pathName: String): String = run {
    //获取配置文件的对象
    FileReader(pathName).use {
        load(it)//将输入流加载到配置文件对象中
    }//获取输入流
    getProperty(key)//返回根据key获取到的value值
}
