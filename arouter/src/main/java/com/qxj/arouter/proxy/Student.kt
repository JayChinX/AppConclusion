package com.qxj.arouter.proxy

class Student constructor(private var name: String) : Person {
    public constructor() : this("") {
        println("获取到无参构造方法")
    }
    constructor(name: String, age: Int) : this(name) {
        println("获取到多参构造方法")
    }
    protected constructor(n: Boolean) : this() {
        println("获取到受保护的构造方法")
    }
    private constructor(age: Int) : this("年龄$age 的学生") {
        println("获取到私有的构造方法")
    }

    override fun giveMoney() {
        println("${name}上交班费50元")
    }


}