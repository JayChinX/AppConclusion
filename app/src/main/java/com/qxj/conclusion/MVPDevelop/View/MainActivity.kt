package com.qxj.conclusion.MVPDevelop.View

import android.annotation.SuppressLint
import android.util.Log
import com.qxj.conclusion.AppConfig
import com.qxj.conclusion.ConclusionUtils.LogTool
import com.qxj.conclusion.MVPDevelop.MVP.BaseActivity
import com.qxj.conclusion.MVPDevelop.Presenter.UserContract
import com.qxj.conclusion.MVPDevelop.Presenter.UserPresenter
import com.qxj.conclusion.R
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
    }
    override fun startCheckPermission() {
        Log.d(TAG, "这里不需要检查权限")
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

}