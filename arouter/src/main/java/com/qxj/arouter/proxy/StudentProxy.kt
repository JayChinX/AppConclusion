package com.qxj.arouter.proxy

import java.lang.reflect.Proxy

class StudentProxy(private var target: Any) {

    private val TAG = StudentProxy::class.java.simpleName

    fun <T> create(service: Class<T>, before: () -> Unit, after: () -> Unit): T {
        return Proxy.newProxyInstance(service.classLoader,//选用的类加载器。因为代理的是T，所以一般都会用加载T的类加载器。
                //被代理的类所实现的接口，这个接口可以是多个
                arrayOf(service)) { proxy, method, args ->
            //proxy，代理后的实例对象。
            //method，对象被调用方法。
            //args，调用时的参数。
            before()
            println("调用了 ${method.name} 方法")
            if (method.name == "giveMoney") {
                //方法拦截
                println("拦截到了giveMoney()方法")
            }
            print("StudentProxy 代理")
            val result = method.invoke(target, *args.orEmpty())
            after()
            result
        } as T
    }
}