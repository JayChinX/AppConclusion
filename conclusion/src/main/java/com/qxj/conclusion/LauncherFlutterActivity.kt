package com.qxj.conclusion

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import io.flutter.facade.Flutter
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterView

class LauncherFlutterActivity : AppCompatActivity() {

    private var flutterView: FlutterView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher_flutter)


        flutterView = with(initFlutterView("route1")) {
            initMethodChannel(this)
            return@with this
        }
    }

    private fun initFlutterView(router: String): FlutterView {
//        val tx = supportFragmentManager.beginTransaction();
//        tx.replace(R.id.someContainer, Flutter.createFragment("route1"))
//        tx.commit()
        val flutterView = Flutter.createView(this, lifecycle, router)
        return flutterView.apply {
            val layout = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            addContentView(this, layout)
        }

    }

    private var methodChannel: MethodChannel? = null

    /**
     * 初始化MethodChannel
     * @param flutterView FlutterView对象
     */
    private fun initMethodChannel(flutterView: FlutterView) {
        methodChannel = MethodChannel(flutterView, "")
        methodChannel?.setMethodCallHandler { methodCall, result ->
            when (methodCall.method) {
                "activityFinish" -> onFlutterBackgroud() //关闭当前页面
            }
        }
    }

    private fun onFlutterBackgroud() {
//        overridePendingTransition()
    }

    override fun onBackPressed() {
        this.flutterView?.popRoute() ?: super.onBackPressed()

    }

}
