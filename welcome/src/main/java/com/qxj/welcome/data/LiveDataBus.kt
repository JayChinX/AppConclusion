package com.qxj.welcome.data


import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlin.reflect.KClass


class LiveDataBus private constructor() {

    private val TAG = LiveDataBus::class.java.simpleName

    private val bus: HashMap<String, MutableLiveData<*>> = HashMap()

    companion object {
        fun get() : LiveDataBus = DEFAULT_BUS
        private val DEFAULT_BUS : LiveDataBus by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            LiveDataBus()
        }
    }

    @Synchronized
    fun <T : Any> with(key: String, type: KClass<T>): MutableLiveData<T> {
        if (!bus.containsKey(key)) {
            Log.d(TAG, "注册$type 的监听器")
            bus[key] = BusMutableLiveData<T>()
        }
        return bus[key] as MutableLiveData<T>
    }

    fun with(key: String): MutableLiveData<Any> {
        return with(key, Any::class)
    }

    private inner class ObserverWrapper<T> constructor(private var observer: Observer<T>) : Observer<T> {

        override fun onChanged(t: T?) {
            if (isCallOnObserver()) {
                return
            }
            observer.onChanged(t)

        }

        private fun isCallOnObserver(): Boolean {
            val stackTrace = Thread.currentThread().stackTrace
            if (stackTrace != null && stackTrace.isNotEmpty()) {
                stackTrace.forEach {
                    if ("android.arch.lifecycle.LiveData" == it.className &&
                            "observeForever" == it.methodName)
                        return true
                }
            }
            return false
        }
    }

    inner class BusMutableLiveData<T> : MutableLiveData<T>() {
        private val observerMap = HashMap<Observer<in T>, Observer<in T>>()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            hook(observer)
        }

        override fun observeForever(observer: Observer<in T>) {
            if (!observerMap.containsKey(observer)) {
                observerMap[observer] = ObserverWrapper(observer)
            }
            super.observeForever(observerMap[observer] as Observer<in T>)
        }

        override fun removeObserver(observer: Observer<in T>) {
            val realObserver: Observer<in T> = if (observerMap.containsKey(observer)) {
                observerMap.remove(observer) as Observer<in T>
            } else {
                observer
            }
            super.removeObserver(realObserver)
        }

        @Throws(NoSuchFieldException::class)
        private fun hook(@NonNull observer: Observer<in T>) {
            val classLiveData = LiveData::class.java
            val fieldObservers = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val objectObservers = fieldObservers.get(this)
            val classObservers = objectObservers.javaClass
            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            if (objectWrapper == null) {
                throw NullPointerException("Wrapper can not be bull!")
            }
            val classObserverWrapper = objectWrapper.javaClass.superclass as Class<*>
            val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
            fieldLastVersion.isAccessible = true
            //get livedata's version
            val fieldVersion = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion.get(this)
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, objectVersion)
        }
    }
}