package com.hymnal.welcome.ui.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.hymnal.welcome.R

/**
 * 点击url跳转到Activity并传递参数
 */
@Route(path = "/home/web/URLWebActivity")
class URLWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urlweb)
    }
//
//    <h2>1:URL普通跳转</h2>
//     //a 标签中的arouter://zhaoyanjun 分别代表 scheme、host参数，/com/URLActivity1 为目标Activity的注解
//    <p><a href="arouter://zhaoyanjun/com/URLWebActivity">arouter://zhaoyanjun/com/URLActivity1 </a>
//    </p>
//
//    <h2>2:URL普通跳转携带参数</h2>
//
//    <p>
//    <a href="arouter://zhaoyanjun/com/URLWebTwoActivity?name=alex&age=18&boy=true&high=180&obj=%7b%22name%22%3a%22jack%22%2c%22id%22%3a666%7d">arouter://zhaoyanjun/test/URLActivity2?name=alex&age=18&boy=true&high=180&obj={"name":"jack","id":"666"}
//    </a>
//    </p>


}
