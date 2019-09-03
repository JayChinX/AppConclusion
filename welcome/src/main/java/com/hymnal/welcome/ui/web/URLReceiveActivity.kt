package com.hymnal.welcome.ui.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.hymnal.welcome.R

/**
 * URL中转Activity
 */
@Route(path = "/home/web/URLReceiveActivity")
class URLReceiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urlreceive)

        //对URL数据进行分发
        val uri = intent.data
        ARouter.getInstance().build(uri).navigation(this, object : NavCallback() {
            override fun onArrival(postcard: Postcard?) {
                finish()
            }

        })
    }
}
