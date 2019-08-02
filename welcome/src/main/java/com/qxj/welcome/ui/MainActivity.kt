package com.qxj.welcome.ui

import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.welcome.R
import com.qxj.welcome.base.BaseActivity
import com.qxj.welcome.ui.second.Author
import com.qxj.welcome.service.IService

/**
 * 参考文献
 *
 * https://blog.csdn.net/u010696783/article/details/79362173
 *
 * 优势：
 *
 * 支持直接解析标准URL进行跳转，并自动注入参数到目标页面中
 * 支持多模块工程使用
 * 支持添加多个拦截器，自定义拦截顺序
 * 支持依赖注入，可单独作为依赖注入框架使用
 * 支持InstantRun
 * 支持MultiDex(Google方案)
 * 映射关系按组分类、多级管理，按需初始化
 * 支持用户指定全局降级与局部降级策略
 * 页面、拦截器、服务等组件均自动注册到框架
 * 支持多种方式配置转场动画
 * 支持获取Fragment
 * 完全支持Kotlin以及混编
 *
 *
 * 典型的应用：
 *
 * 从外部URL映射到内部页面，以及参数传递与解析
 * 跨模块页面跳转，模块间解耦
 * 拦截跳转过程，处理登陆、埋点等逻辑
 * 跨模块API调用，通过控制反转来做组件解耦
 */
//在支持路由的页面上添加注解
//path 路径至少需要两级 /xx/xx 其中com为分组标识  后面的为类标识
@Route(path = "/home/activity/MainActivity")
class MainActivity : BaseActivity() {
    override fun subscribeUi() {

    }

    private val TAG = MainActivity::class.java.simpleName

    override fun initView() {
        /**
         * 启动时间查看
         *
         */
        //4.4 以前 adb shell am start -W packName/activity(全路径) 查看
        //4.4 以后 Displayed 关键字查看
        //Debug.startMethodTracing()
        //运行时间
        //trace 文件在adb pull /storage/emulated/0/appcation_launcher_time.trace
        //Debug.stopMethodTracing()
        //IntentService onHandleIntent() 方法进行初始化一些比较耗时的操作
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    private fun toSecondActivity() {
        //发起路由跳转
        ARouter.getInstance().build("/arouter/activity/SecondActivity").navigation()

    }

    private fun toSecondActivityOther() {
        //带参数的跳转
        ARouter.getInstance().build("/arouter/activity/SecondActivity")
                .withString("name", "123")
                .withBoolean("is", true)
                .navigation()

    }

    private fun toSecondActivityForResult() {
        //带返回值的跳转
        ARouter.getInstance().build("/arouter/activity/SecondActivity").navigation(this, 10)

    }

    /**
     * ARouter框架是分组管理，按需加载。
     * 解释起来就是，在编译期框架扫描了所有的注册页面／服务／字段／拦截器等，
     * 那么很明显运行期不可能一股脑全部加载进来，这样就太不和谐了。所以就分组来管理，
     * ARouter在初始化的时候只会一次性地加载所有的root结点，而不会加载任何一个Group结点，
     * 这样就会极大地降低初始化时加载结点的数量。
     * 比如某些Activity分成一组，组名就叫test，然后在第一次需要加载组内的某个页面时再将test这个组加载进来。
     */
    private fun toSecondActivityCallback() {
        ARouter.getInstance().build("/arouter/activity/SecondActivity").navigation(this, object : NavCallback() {
            override fun onArrival(postcard: Postcard?) {
                Log.e(TAG, "onArrival: 跳转完毕")
                //分组group
                val group = postcard!!.group
            }

            override fun onFound(postcard: Postcard?) {
                Log.e(TAG, "onFound: 找到url")

            }

            override fun onLost(postcard: Postcard?) {
                Log.e(TAG, "onLost: 没有找到url")

            }

            override fun onInterrupt(postcard: Postcard?) {
                Log.e(TAG, "onInterrupt: 被拦截")

            }
        })

    }

    //自定义分组
    private fun toCustomGroupActivity() {
        ARouter.getInstance().build("/arouter/activity/CustomGroupActivity").navigation()
    }

    /**
     * Fragment路由
     */
    private fun getFragment(): Fragment {
        val fragment = ARouter.getInstance().build("/arouter/fragment/OneFragment").navigation() as Fragment
        return fragment
    }

    /**
     * 传递自定义参数 需要实现 JsonServiceImpl : SerializationService
     */

    private fun toSecondActivityOthers() {
        val author = Author("qxj", 20, "zz")
        //带参数的跳转
        ARouter.getInstance().build("/arouter/activity/SecondActivity")
                .withString("name", "123")
                .withObject("author", author)
                .withBoolean("is", true)
                .navigation()

    }

    /**
     * web url跳转路由
     *
     * url跳转原生activity页面
     */

    /**
     * 发现服务
     *
     * 这里的服务是接口开发的概念，就是将一部分功能和组件封装起来成为接口，以接口的形式对外提供能力，
     * 所以在这部分就可以将每个功能作为一个服务，而服务的实现就是具体的业务功能。
     * 发现服务这个功能的特点在于，我们只需要知道接口，不需要关心接口的实现类，很好了实现了解耦
     */
    @Autowired(name = "/arouter/service/hello")//CustomService的路由
    lateinit var service1: IService

    var service2: IService? = null

    var service3: IService? = null

    //添加注解初始化自动赋值
    private fun initService() {
        //1.自动注入
        ARouter.getInstance().inject(this)
        service1.sayHello(this)
        //获取服务的方式
        //2.by name
        service2 = ARouter.getInstance().navigation(IService::class.java)
        //3.by path
        service3 = ARouter.getInstance().build("/service/hello").navigation() as IService
    }

    /**
     * 拦截器 InterceptorImpl
     *
     * ARouter自带的拦截器功能，每个组件都需要定义一个拦截器，当组件卸载之后需要拦截住该组件的跳转入口。
     *
     * 路由跳转的执行顺序为，先执行回调函数的onFound()，之后是拦截器2的process()，拦截器1的process()，
     * 最后执行回调函数的onArrival()。
     * 注意：拦截器是按照优先级的高低进行顺序执行的，优先级也高，越早执行。
     */

    /**
     * -keep public class com.alibaba.android.arouter.routes.**{*;}
     * -keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
     */

}
