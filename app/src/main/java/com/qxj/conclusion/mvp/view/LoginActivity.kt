package com.qxj.conclusion.mvp.view

import android.content.Intent
import android.view.View
import com.qxj.conclusion.AppConfig
import com.qxj.conclusion.util.LogTool
import com.qxj.conclusion.mvp.base.BaseActivity
import com.qxj.conclusion.mvp.presenter.LoginContract
import com.qxj.conclusion.mvp.presenter.LoginPresenter
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), LoginContract.LoginView,
        View.OnClickListener {

    private val TAG: String = LoginActivity::class.java.name

    override val mPresenter: LoginPresenter = LoginPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {

        name_user.setType(1)
        paw_user.setType(2)
        dialog.setOnClickListener(this)
        dialog1.setOnClickListener(this)
        login_user.setOnClickListener(this)

    }

    override fun loginSuccess(msg: String) {
        toast(msg)
        val intent = Intent()
        intent.setClass(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun loginFiled(msg: String) {
        toast(msg)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.login_user -> {
                mPresenter.login(name_user.text.toString(), paw_user.text.toString())
            }
            R.id.dialog -> {

            }
            R.id.dialog1 -> {

            }
        }
    }

}