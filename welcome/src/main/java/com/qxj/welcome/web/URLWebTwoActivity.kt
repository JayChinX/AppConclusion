package com.qxj.welcome.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.welcome.R

/**
 * 从url中获取参数
 */
@Route(path = "/home/web/URLWebTwoActivity")
class URLWebTwoActivity : AppCompatActivity() {

    @Autowired
    @JvmField
    var name: String? = null

    @Autowired
    @JvmField
    var age: Int = -1

    @Autowired
    @JvmField
    var boy = false

    @Autowired
    @JvmField
    var high: Int = -1

    @Autowired
    @JvmField
    var obj: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.activity_urlweb_two)

        //解析参数
        val bundle = intent.extras
        name = bundle.getString("name")
    }
}
