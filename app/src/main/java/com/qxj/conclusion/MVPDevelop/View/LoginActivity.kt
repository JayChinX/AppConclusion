package com.qxj.conclusion.MVPDevelop.View

import android.content.Intent
import android.util.Log
import android.view.View
import com.qxj.conclusion.ConclusionUtils.LogTool
import com.qxj.conclusion.ConclusionUtils.PermissionUtil
import com.qxj.conclusion.MVPDevelop.MVP.BaseActivity
import com.qxj.conclusion.MVPDevelop.Presenter.LoginContract
import com.qxj.conclusion.MVPDevelop.Presenter.LoginPresenter
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), LoginContract.ILoginView, View.OnClickListener {

    private val TAG: String = LoginActivity::class.java.name

    override val mPresenter: LoginPresenter = LoginPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        dialog.setOnClickListener(this)
        dialog1.setOnClickListener(this)
        login_user.setOnClickListener(this)

    }

    override fun showLoginUserResult(boolean: Boolean, msg: String) {
        if (boolean) {
            toast(msg)
            val intent = Intent()
            intent.setClass(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            toast(msg)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.login_user -> {
                userName = name_user.text.toString()
                userPassword = paw_user.text.toString()
                mPresenter.toLoginUser(userName, userPassword, "")
            }
            R.id.dialog -> {
                this.showAlertDialog("警告", "网络连接失败！", "取消", "重新连接", true,
                        {
                            LogTool.d(TAG, "点击了取消")

                        }, {
                    LogTool.d(TAG, "点击了重新连接")
                    val intent = Intent()
                    intent.setClass(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                })
            }
            R.id.dialog1 -> {
                //trimMargin()//删除前置空格  trimMargin("..")删除字符
                this.showAlertDialog("更新提醒", """
                    |更新内容：
                    |   1.修复BUG。
                    |   2.更新新特性
                    """.trimMargin(), null, "现在更新", false, null) {
                    LogTool.d(TAG, "现在更新")
                    val intent = Intent()
                    intent.setClass(applicationContext, PermissionActivity::class.java)
                    intent.putExtra("id", 1234)
                    startActivity(intent)

                }
            }
        }
    }

    override fun startCheckPermission() {
        Log.d(TAG, "这里不需要检查权限")
    }
}