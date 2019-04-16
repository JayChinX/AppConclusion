package com.qxj.conclusion.mvp.presenter

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import com.qxj.commonsdk.network.*
import com.qxj.commonbase.mvpbase.IPresenter
import com.qxj.conclusion.mvp.model.*
import com.qxj.conclusion.mvvm.model.UserBean
import io.reactivex.Observable
import kotlinx.coroutines.*
import org.jetbrains.anko.custom.async
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class LoginPresenter(view: LoginContract.LoginView) :
        LoginContract.LoginPresenter,
        IPresenter<LoginContract.LoginView>(view) {

    private val TAG: String = LoginPresenter::class.java.name

    private lateinit var model: LoginModel

    private lateinit var owner: LifecycleOwner

    override fun initData(intent: Intent) {
        val type = intent.getStringExtra("type")
        model = LoginModel()
    }

    override fun initLifecycle(owner: LifecycleOwner) {
        this.owner = owner
    }

    @SuppressLint("CheckResult")
    override fun login(name: String, password: String) {
        //用户名和密码检查合法性
        if (!model.checkLogin(name, password) { msg: String ->
                    mView.get()?.loginFiled(msg)
                }) return

        val map = HashMap<String, String>()
        map["userId"] = "H2409761"
        ApiService.getUserLocation<UserBean>(Gson().toRequestBody(map))
                .async(100)//扩展函数
                .doOnSubscribe {
                    //开始网络请求
                    mView.get()?.showLoading()
                }
                .doAfterTerminate {
                    //网络请求完毕
                    mView.get()?.hideLoading()
                }
                .bindLifecycle(owner)//扩展函数绑定生命周期
                .subscribe({ data ->
                    data?.let {
                        it.message.let {
                            Log.d(TAG, "请求成功${it!![0].name}")
                        }
                    }
                }, { e: Throwable ->

                })
        ApiService.login<UserBean>(name, password)
                .async(100)//扩展函数
                .doOnSubscribe {
                    //开始网络请求
                    mView.get()?.showLoading()
                }
                .doAfterTerminate {
                    //网络请求完毕
                    mView.get()?.hideLoading()
                }
                .bindLifecycle(owner)//扩展函数绑定生命周期
                .subscribes({

                }, {

                })//自定义范围数据拦截器

        /**
         * RxJava
         */
        Observable.create<Int> {
            it.onNext(1)
        }.subscribe {
            Log.d(TAG, "  $it")
        }

        Observable.range(0, 10).map {
            it.toString()
        }.forEach {
            Log.d(TAG, "  $it")
        }

        /**
         * 1.创建操作符
         *      1）interval 去区间间隔 intervalRange
         *          构造方法
         *          initialDelay第一次等待多少时间后开始发送  period 时间间隔  unit单位
         *          public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler)
         *          public static Observable<Long> interval(long period, TimeUnit unit, Scheduler scheduler)
         *          start 发送的起始值  count  发送多少次
         *          public static Observable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit, Scheduler scheduler)
         *      2）range  rangeLong  发送的类型 Int Long
         *      3）create
         *      4）defer
         *          直到有观察者订阅时才会创建 Observable
         *          defer 操作符会一直等待直到有观察者订阅为止
         *          public static <T> Observable<T> defer(Callable<? extends ObservableSource<? extends T>> supplier)
         *      5）empty never error
         *          public static <T> Observable<T> empty()：创建一个不发射任何数据但是正常终止的Observable；
         *          public static <T> Observable<T> never()：创建一个不发射数据也不终止的Observable；
         *          public static <T> Observable<T> error(Throwable exception)：创建一个不发射数据以一个错误终止的Observable，它有几个重载版本，这里给出其中的一个。
         *      6）from  从指定的数据源中获取一个 observable
         *          构造方法
         *          数组
         *          public static <T> Observable<T> fromArray(T... items)
         *          Callable
         *          public static <T> Observable<T> fromCallable(Callable<? extends T> supplier)
         *          Future
         *          public static <T> Observable<T> fromFuture(Future<? extends T> future)
         *          Iterable
         *          public static <T> Observable<T> fromIterable(Iterable<? extends T> source)
         *          Publisher
         *          public static <T> Observable<T> fromPublisher(Publisher<? extends T> publisher)
         *      7）just  从一组数据中获取
         *          public static <T> Observable<T> just(T item)
         *      8）repeat 表示指定的序列发射多少次
         *          无参  无限次
         *          public final Observable<T> repeat()
         *          public final Observable<T> repeat(long times)
         *          满足指定的条件时停止发送，否则一直发送
         *          public final Observable<T> repeatUntil(BooleanSupplier stop)
         *          public final Observable<T> repeatWhen(Function<? super Observable<Object>, ? extends ObservableSource<?>> handler)
         *      10）timer
         *          延迟一定的时间后开始发送 0
         *          public static Observable<Long> timer(long delay, TimeUnit unit)
         *          public static Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler)
         *
         */
        //每隔三秒发送一个整数 0.....
        Observable.interval(3, TimeUnit.SECONDS).subscribe {
            Log.d(TAG, "   $it")
        }
        //从 5 开始的连续 10 个整数序列
        Observable.range(5, 10).subscribe {

        }
        //重复发送 从 5 开始的 10个整数序列
        Observable.range(5, 10).repeat().subscribe {

        }
        //create 用于从头开始创建一个 Observable
        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onComplete()
            it.onError(Throwable())
        }.subscribe {
            Log.d(TAG, "  $it")
        }

        Observable.timer(500, TimeUnit.MILLISECONDS).subscribe {
            Log.d(TAG, " 延迟500毫秒发送一个数字0 == $it ")
        }

        /**
         * 2.变换操作符
         *      1）map  cast
         *          map: 对原始 Observable 发射的每一个数据应用一个选择的函数，并返回一个发射这些结果的
         *          observable
         *          cast: 对原始 Observable 发射的每一个数据都强制转换为一个指定的类型，然后再发射数据
         *      2）flatMap contactMap
         *          flatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们
         *          发射的事件合并后放进一个单独的Observable里。需要注意的是, flatMap并不保证事件的顺序，
         *          也就是说转换之后的Observables的顺序不必与转换之前的序列的顺序一致。
         *      3）flatMapIterable
         *          将上流的任意一个元素转换成一个Iterable对象
         *          1. public final <U> Observable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper)
         *          2. public final <U, V> Observable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper,
         *              BiFunction<? super T, ? super U, ? extends V> resultSelector)
         *      4）buffer
         *          用于将整个流进行分组
         *          1. public final Observable<List<T>> buffer(int count)
         *          2. public final Observable<List<T>> buffer(int count, int skip)
         *      5）groupBy
         *          用于分组元素
         *      6）scan
         *          对原始Observable发送的第一个数据应用一个函数，然后将函数的结果作为第一项数据发送。将函数的结果同第二项数据
         *          一起填充给这个函数产生第二项数据。
         *      7）window
         *          window 和 buffer 类似，但不是发射原始observable的数据包，它发射的是Observable，这些observable的每一个都
         *          发射原始observable数据的一个子集，最后发射一个onCompleted通知
         *          1. public final Observable<Observable<T>> window(long count)
         *          2. public final Observable<Observable<T>> window(long timespan, long timeskip, TimeUnit unit)
         *          3. public final <B> Observable<Observable<T>> window(ObservableSource<B> boundary)
         *          4. public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> boundary)
         *
         */

        Observable.range(1, 5).map { it.toString() }.subscribe {

        }

        Observable.just(Date()).cast(Object::class.java).subscribe {

        }

        //flatMap 将一个序列构成的整数 装换成多个 Observable, 然后组成一个 Observable并被订阅，
        //          输出的顺序不保证
        //contactMap 保证了输出的顺序
        Observable.range(1, 5).flatMap {
            Observable.just(it.toString())
        }.subscribe {

        }

        //flatMapIterable
        Observable.range(1, 5).flatMapIterable {
            Collections.singletonList(it.toString())
        }.subscribe {
            Log.d(TAG, ".....$it")
        }

        //buffer  从1开始的7个数，buffer 之后数据会 3 个一组输出
        Observable.range(1, 7).buffer(3).subscribe {
            Log.d(TAG, "...${Arrays.toString(it.toIntArray())}")
        }

        //groupBy 分组
        Observable.concat(
                Observable.concat(Observable.range(1, 4), Observable.range(1, 6))
                        .groupBy {
                            it
                        }
        ).subscribe {
            Log.d(TAG, "groupBy....$it")
        }

        //scan
        Observable.range(2, 5).scan { t1: Int, t2: Int ->
            t1 * t2
        }.subscribe {
            Log.d(TAG, "scan:  $it")
        }

        Observable.range(2, 5).scan(3) { t1: Int, t2: Int ->
            t1 * t2
        }.subscribe {
            Log.d(TAG, "scan:  $it")
        }

        //window  10个数3个一组，每组组成observable，再发射
        Observable.range(1, 10).window(3)
                .subscribe { it ->
                    it.subscribe {
                        Log.d(TAG, "....$it")
                    }
                }

        /**
         * 3.过滤操作符
         *      1）filter
         *          根据指定规则对源数据进行过滤
         *      2）elementAt  firstElement  lastElement
         *          elementAt 获取源数据中指定位置的数据
         *          firstElement 获取第一个元素
         *          lastElement 获取最后一个元素
         *      3）distinct 去重 distinctUntilChanged 去掉相邻重复
         *      4）skip skipLast skipUnit skipWhile
         *          skip 过滤掉数据的前n项
         *
         *
         */

        //filter
        Observable.range(1, 10).filter {
            it > 5
        }.subscribe {

        }

        //elementAt
        Observable.range(0, 10).elementAt(0)
                .subscribe {

                }


    }

    /**
     * kotlin 协程
     */
    private fun getUser() = runBlocking {
        launch(Dispatchers.Main) {
            val user = async(Dispatchers.IO) {
                "main"
            }.await()

            println(user)
        }
    }

    //runBlocking
    private fun getUser(textView: TextView) {
        //launch
        GlobalScope.launch(Dispatchers.Main) {
            //async
            textView.text = async(AndroidCommonPool) {
                return@async "main"
            }.await()


        }
    }

}

/**
 * kotlin 协程
 */
private fun coroutines() {

    GlobalScope.launch {
        delay(1000L)
        println("good")
    }
    println("Hello,")
    Thread.sleep(2000L)
}

fun main() = runBlocking {
//    coroutines()
//    getUser()
}




object AndroidCommonPool: CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(block)
    }

}
//
//fun main() = runBlocking {
//    //    launch {
////        delay(1000L)
////        println("good")
////        doWorld()
////    }
////    println("hello")
//
//    repeat(10_000) {
//        launch {
//            delay(1000L)
//            print(".")
//        }
//    }
//}
//
//suspend fun doWorld() {
//    delay(1000L)
//    println("world")
//}

