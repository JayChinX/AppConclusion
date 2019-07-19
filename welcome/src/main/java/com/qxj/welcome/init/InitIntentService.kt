package com.qxj.welcome.init

import android.app.Activity
import android.app.Application
import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.qxj.welcome.utilities.Navigation


class InitIntentService : IntentService("InitIntentService") {

    private val TAG = InitIntentService::class.java.simpleName

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1, param2)
            }
            ACTION_BAZ -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionBaz(param1, param2)
            }
            ACTION_INIT -> handleActionInit()
        }
    }

    private fun handleActionFoo(param1: String, param2: String) {
    }

    private fun handleActionBaz(param1: String, param2: String) {
    }

    private fun handleActionInit() {
        /** 蒲公英初始化 **/
//        Log.d(TAG, "初始化 蒲公英 PgyCrashManager")
//        PgyCrashManager.register()
//        PgyCrashManager.setIsIgnoreDefaultHander(true) //默认设置为false;
        //设置为 true ,则忽略系统默认Crash 操作，SDK 会重启启动 app 的当前页面

        /** bugly **/
        Log.d("InitIntentService", "初始化 bugly")
//        Bugly.init(application, "2c5d479957", true)

        Log.d(TAG, "初始化 LogXixi")
//        LogXixi.init(application)

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//        val cb = object : QbSdk.PreInitCallback {
//
//            override fun onViewInitFinished(arg0: Boolean) {
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                //                LogAndroid.d("app", " onViewInitFinished is " + arg0);
//            }
//
//            override fun onCoreInitFinished() {}
//        }
//        Log.d(TAG, "初始化 QbSdk")
//        //x5内核初始化接口
//        QbSdk.initX5Environment(applicationContext, cb)
    }

    companion object {

        private const val ACTION_FOO = "com.geely.gic.hmi.ui.main.action.FOO"
        private const val ACTION_BAZ = "com.geely.gic.hmi.ui.main.action.BAZ"
        private const val ACTION_INIT = "com.geely.gic.hmi.ui.main.action.INIT"
        private const val EXTRA_PARAM1 = "com.geely.gic.hmi.ui.main.extra.PARAM1"
        private const val EXTRA_PARAM2 = "com.geely.gic.hmi.ui.main.extra.PARAM2"
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, InitIntentService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, InitIntentService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }

        fun startActionInit(context: Context) {
            val intent = Intent(context, InitIntentService::class.java).apply {
                action = ACTION_INIT
            }
            context.startService(intent)
        }


    }
}

fun Application.init(application: Application) {
    Navigation.getInstance().init(application)
    InitIntentService.startActionInit(application)
}

fun Application.attachInit(context: Context) {
    // you must install multiDex whatever tinker is installed!
//            MultiDex.install(context)

    // 安装tinker
//            Beta.installTinker()
}

fun Activity.startInit(activity: Activity) {
    Navigation.getInstance().inject(activity)

    /**
     * 蒲公英 反馈UI配置 start
     */

    /** Dailog UI 显示反馈 **/
    // 默认采用摇一摇弹出 Dialog 方式
//            PgyerFeedbackManager.PgyerFeedbackBuilder()
//                    .builder()
//                    .register()


    // 可自定义的选项
//            PgyerFeedbackManager.PgyerFeedbackBuilder()
//                    .setShakeInvoke(false)  //设置是否摇一摇的方式激活反馈，默认为 true
//                    // fasle 则不触发摇一摇，最后需要调用 invoke 方法
//                    .setColorDialogTitle("#FF0000")    //设置Dialog 标题的字体颜色，默认为颜色为#ffffff
//                    .setColorTitleBg("#FF0000")        //设置Dialog 标题栏的背景色，默认为颜色为#2E2D2D
//                    // 默认参数为PgyerFeedbackManager.TYPE.DIALOG_TYPE, Dialog UI 显示
//                    // 可选参数PgyerFeedbackManager.TYPE.ACTIVITY_TYPE  Activity UI 显示
//                    .setDisplayType(PgyerFeedbackManager.TYPE.DIALOG_TYPE)
//                    .setMoreParam("KEY1", "VALUE1") //自定义的反馈数据
//                    .setMoreParam("KEY2", "VALUE2") //自定义的反馈数据
//                    .builder()
//                    .register()
    /** 自定义触发反馈 Dailog UI 显示反馈 **/


//            PgyerFeedbackManager.PgyerFeedbackBuilder()
//                    .setShakeInvoke(false)       //fasle 则不触发摇一摇，最后需要调用 invoke 方法
//                    // true 设置需要调用 register 方法使摇一摇生效
//                    .setDisplayType(PgyerFeedbackManager.TYPE.DIALOG_TYPE)   //设置以Dialog 的方式打开
//                    .setColorDialogTitle("#FF0000")    //设置Dialog 标题的字体颜色，默认为颜色为#ffffff
//                    .setColorTitleBg("#FF0000")        //设置Dialog 标题栏的背景色，默认为颜色为#2E2D2D
//                    .setBarBackgroundColor("#FF0000")      // 设置顶部按钮和底部背景色，默认颜色为 #2E2D2D
//                    .setBarButtonPressedColor("#FF0000")        //设置顶部按钮和底部按钮按下时的反馈色 默认颜色为 #383737
//                    .setColorPickerBackgroundColor("#FF0000")   //设置颜色选择器的背景色,默认颜色为 #272828
//                    .setMoreParam("KEY1", "VALUE1") //自定义的反馈数据
//                    .setMoreParam("KEY2", "VALUE2") //自定义的反馈数据
//                    .builder()
//                    .invoke();                  //激活直接显示的方式



    /**Activity UI 显示反馈 **/
    // 采用摇一摇弹出 Activity 方式
//            PgyerFeedbackManager.PgyerFeedbackBuilder()
//                    .setDisplayType(PgyerFeedbackManager.TYPE.DIALOG_TYPE)
//                    .builder()
//                    .register()

//            PgyerFeedbackManager.PgyerFeedbackBuilder()
//                    .setShakeInvoke(false)       //fasle 则不触发摇一摇，最后需要调用 invoke 方法
//                    // true 设置需要调用 register 方法使摇一摇生效
//                    .setBarBackgroundColor("#FF0000")      // 设置顶部按钮和底部背景色，默认颜色为 #2E2D2D
//                    .setBarButtonPressedColor("#FF0000")        //设置顶部按钮和底部按钮按下时的反馈色 默认颜色为 #383737
//                    .setColorPickerBackgroundColor("#FF0000")   //设置颜色选择器的背景色,默认颜色为 #272828
//                    .setBarImmersive(true)              //设置activity 是否以沉浸式的方式打开，默认为 false
//                    .setDisplayType(PgyerFeedbackManager.TYPE.ACTIVITY_TYPE)   //设置以 Activity 的方式打开
//                    .setMoreParam("KEY1", "VALUE1")      //自定义的反馈数据
//                    .setMoreParam("KEY2", "VALUE2")      //自定义的反馈数据
//                    .builder()
//                    .invoke();                  //激活直接显示的方式

    /**
     * 蒲公英 反馈UI配置  end
     */

    /**
     * 蒲公英 应用更新配置
     */

    //检查更新
    /** 新版本 **/
    /** 默认方式 **/
//            PgyUpdateManager.Builder()
//                    .register()

    /** 可选配置集成方式 **/
//            PgyUpdateManager.Builder()
//                    .setForced(false)                //设置是否强制提示更新
//                    // v3.0.4+ 以上同时可以在官网设置强制更新最高低版本；网站设置和代码设置一种情况成立则提示强制更新
//                    .setUserCanRetry(false)         //失败后是否提示重新下载
//                    .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
//                    .register()

    /** 带回调的更新 **/

    /** 新版本 **/
//            PgyUpdateManager.Builder()
//                    .setForced(true)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
//                    .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
//                    .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
//                    .setUpdateManagerListener(object : UpdateManagerListener {
//                        override fun onNoUpdateAvailable() {
//                            //没有更新是回调此方法
//                            Log.d("pgyer", "there is no new version")
//                        }
//
//                        override fun onUpdateAvailable(appBean: AppBean) {
//                            //有更新回调此方法
//                            Log.d("pgyer", "there is new version can update"
//                                    + "new versionCode is " + appBean.versionCode)
//                            //调用以下方法，DownloadFileListener 才有效；
//                            //如果完全使用自己的下载方法，不需要设置DownloadFileListener
//                            PgyUpdateManager.downLoadApk(appBean.downloadURL)
//                        }
//
//                        override fun checkUpdateFailed(e: Exception) {
//                            //更新检测失败回调
//                            //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
//                            Log.e("pgyer", "check update failed ", e)
//                        }
//                    })
//                    //注意 ：
//                    //下载方法调用 PgyUpdateManager.downLoadApk(appBean.getDownloadURL()); 此回调才有效
//                    //此方法是方便用户自己实现下载进度和状态的 UI 提供的回调
//                    //想要使用蒲公英的默认下载进度的UI则不设置此方法
//                    .setDownloadFileListener(object : DownloadFileListener {
//                        override fun onProgressUpdate(vararg p0: Int?) {
//                            Log.e("pgyer", "update download apk progress$p0")                        }
//
//                        override fun downloadFailed() {
//                            //下载失败
//                            Log.e("pgyer", "download apk failed")
//                        }
//
//                        override fun downloadSuccessful(uri: Uri) {
//                            Log.e("pgyer", "download apk failed")
//                            // 使用蒲公英提供的安装方法提示用户 安装apk
//                            PgyUpdateManager.installApk(uri)
//                        }
//                    })
//                    .register()

    /** 混淆 **/
    //-libraryjars libs/pgyer_sdk_x.x.jar
    //-dontwarn com.pgyersdk.**
    //-keep class com.pgyersdk.** { *; }
    //-keep class com.pgyersdk.**$* { *; }
}

fun Fragment.startInit(fragment: Fragment) {
    Navigation.getInstance().inject(fragment)
    // 默认采用摇一摇弹出 Dialog 方式
//            PgyerFeedbackManager.PgyerFeedbackBuilder().builder().register()
}
