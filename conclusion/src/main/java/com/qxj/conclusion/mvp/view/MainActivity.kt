package com.qxj.conclusion.mvp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.qxj.conclusion.AppConfig
import com.qxj.commonsdk.LogTool
import com.qxj.commonbase.mvpbase.BaseActivity
import com.qxj.conclusion.LauncherFlutterActivity
import com.qxj.conclusion.mvp.presenter.UserContract
import com.qxj.conclusion.mvp.presenter.UserPresenter
import com.qxj.conclusion.R
//import com.qxj.conclusion.dagger.FactoryActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity: BaseActivity(), UserContract.IUserView {

    private val TAG: String = MainActivity::class.java.name

    override val mPresenter: UserPresenter = UserPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_main

    @SuppressLint("SetTextI18n")
    override fun initView() {
        tvMsg.text = """
            用户名：${AppConfig.userName}
            密码：    ${AppConfig.userPassword}
        """.trimIndent()
        tvUser.setOnClickListener{
            LogTool.d(TAG, "开始添加名字")
            mPresenter.addUser("秦小杰")

        }

        to_flutter.setOnClickListener {
            Log.d(TAG, "点击进入Flutter_module模块")
            startActivity(Intent(this, LauncherFlutterActivity::class.java))
        }

//        to_dagger.setOnClickListener {
//            Log.d(TAG, "点击进入Flutter_module模块")
//            startActivity(Intent(this, FactoryActivity::class.java))
//        }
    }

    override fun showAddUserResult(boolean: Boolean) {
        LogTool.d(TAG, "添加名字结果")
        if (boolean) {
            tvMsg.text = "秦小杰"
            toast("添加名字成功")
        } else {
            tvMsg.text = "添加失败"
            toast("添加名字失败")
        }
    }

    override fun showLoading() {
        super.showLoading()
    }


}