package com.qxj.arouter.web

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.arouter.R

/**
 * 从url中获取参数
 */
@Route(path = "/com/URLWebTwoActivity")
class URLWebTwoActivity : AppCompatActivity() {

    @Autowired
    var name: String? = null
    @Autowired
    var age: Int = -1
    @Autowired
    var boy = false
    @Autowired
    var high: Int = -1
    @Autowired
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
