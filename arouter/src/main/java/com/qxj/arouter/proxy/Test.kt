package com.qxj.arouter.proxy

import java.io.FileReader
import java.util.*

fun main() {

    /**
     * java 动态代理
     */
    val proxy = StudentProxy(Student("张三"))

    val api = proxy.create(Person::class.java, {
        println("开始代理")
    }, {
        println("代理完成")
    })
    api.giveMoney()

    /**
     * CGLIB 动态代理 android不能用
     */

    /**
     * 反射
     */

    //1.Class对象的获取
    val student = Student("李四")
    val stuClass1 = student.javaClass
    println("${stuClass1.name}")
    //2.
    val stuClass2 = Student::class.java
    println("${stuClass2.name}")
    //3.
    try {
        val stuClass3 = Class.forName("com.qxj.arouter.proxy.Student")
        println("${stuClass3.name}")
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    }

    /**
     * 三种方法获取到的Class对象是一样的
     */
    //获取所有公共构造方法
    val stuClass = Class.forName("com.qxj.arouter.proxy.Student")

    //公共构造方法
    val conArray = stuClass.constructors
    for (item in conArray) {
        println("公共构造方法 $item")
    }

    //所有构造方法
    val conAllArray = stuClass.declaredConstructors
    for (item in conAllArray) {
        println("所有构造方法 $item")
    }

    //公有无参构造方法
    var con = stuClass.getConstructor()
    println("公有无参构造方法 $con")

    //构造了一个无参对象
    val obj = con.newInstance() as Student
    obj.giveMoney()

    //获取私有构造方法
    con = stuClass.getDeclaredConstructor(Int::class.java)
    con.isAccessible = true
    val objects = con.newInstance(10) as Student
    objects.giveMoney()

    /**
     * 反射获取配置文件 key value
     */


}



