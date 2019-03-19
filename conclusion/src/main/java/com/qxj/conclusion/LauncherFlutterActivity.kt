package com.qxj.conclusion

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import io.flutter.facade.Flutter
import io.flutter.plugin.common.*
import io.flutter.view.FlutterView
import org.jetbrains.anko.toast
import java.nio.ByteBuffer

class LauncherFlutterActivity : AppCompatActivity() {

    private val TAG = LauncherFlutterActivity::class.java.simpleName

    private var flutterView: FlutterView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher_flutter)


        flutterView = with(initFlutterView("route1")) {
            initMethodChannel(this)
            return@with this
        }

//        initFlutterFragment("router1")
    }

    private fun initFlutterView(router: String): FlutterView {
        Log.d(TAG, "initFlutterView 初始化")
        val flutterView = Flutter.createView(this, lifecycle, router)
        return flutterView.apply {
            val layout = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            addContentView(this, layout)
        }

    }

    private fun initFlutterFragment(router: String) {
        val flutterView = Flutter.createFragment(router)
        flutterView.apply {
           supportFragmentManager.beginTransaction()
            .replace(R.id.someContainer, this)
            .commit()
        }
    }

    private var methodChannel: MethodChannel? = null
    private var eventChannel: EventChannel? = null
    private var basicChannel: BasicMessageChannel<String>? = null

    /**
     * 接收flutter native/native.dart 调用的方法
     * 初始化MethodChannel
     * @param flutterView FlutterView对象
     *
     * 1.com.simple.channelflutterandroid/method 可以不是包名 对应一致
     * 2.setMethodCallHandler(),针对三种方式一一对应，属于监听
     *      MethodChannel - setMethodCallHandler()
     *      EventChannel - setStreamHandler()
     *      BasicMessageChannel - setMessageHandler()
     *
     *      其中setStreamHandler()方式稍微特殊
     *
     * 3.FlutterView 实现 BinaryMessenger 接口
     */
    private fun initMethodChannel(flutterView: FlutterView) {
        methodChannel = MethodChannel(flutterView, "com.simple.channelflutterandroid/method",
                StandardMethodCodec.INSTANCE)
        //flutter 主动   native被动
        methodChannel?.setMethodCallHandler { methodCall, result ->
            when (methodCall.method) {
                "activityFinish" -> onFlutterBackgroud() //关闭当前页面
                "toast" -> {
                    //"接收到消息"
                    val msg = methodCall.argument<String>("msg")
                    //接收后的动作
                    toast(msg!!)
                    //回调给flutter层
                    result.success("native android toast success")
                }
            }
        }
    }

    private fun initEventChannel(flutterView: FlutterView) {
        eventChannel = EventChannel(flutterView, "com.simple.channelflutterandroid/event")
        eventChannel?.setStreamHandler(object : EventChannel.StreamHandler{
            override fun onListen(p0: Any?, p1: EventChannel.EventSink) {
                p1.success("native 发送的消息")
            }

            override fun onCancel(p0: Any?) {
            }
        })
    }

    private fun initBasicMessageChannel(flutterView: FlutterView) {
        basicChannel = BasicMessageChannel<String>(flutterView, "com.simple.channelflutterandroid/basic",
                StringCodec.INSTANCE)
        //发送消息
        basicChannel?.send("")
        //接收消息
        basicChannel?.setMessageHandler { msg, reply ->
            //接收到的消息
            toast(msg)
            //回复消息
            reply.reply("")
        }
    }

    private fun initByte(flutterView: FlutterView) {
        //binary
        flutterView.send("com.simple.channelflutterandroid/basic/binary", ByteBuffer.allocate(225))
        flutterView.setMessageHandler("com.simple.channelflutterandroid/basic/binary") { byteBuffer, binary ->
            //接收到的消息
            toast("接收到的消息 $byteBuffer")
            //回复
            binary.reply(ByteBuffer.allocate(225))
        }
    }


    private fun onFlutterBackgroud() {
//        overridePendingTransition()
    }

    override fun onBackPressed() {
        this.flutterView?.popRoute() ?: super.onBackPressed()

    }

}
