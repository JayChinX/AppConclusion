package com.qxj.conclusion.mvp.view

import com.qxj.commonbase.mvpbase.BaseActivity
import com.qxj.conclusion.mvp.presenter.PermissionContract
import com.qxj.conclusion.mvp.presenter.PermissionPresenter
import com.qxj.conclusion.R
import org.jetbrains.anko.toast

class PermissionActivity : BaseActivity(), PermissionContract.IPermissionView {

    private val TAG: String = PermissionActivity::class.java.name

    override val mPresenter: PermissionPresenter = PermissionPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_permission

    override fun initView() {

        val id = intent.getIntExtra("id", 0)
        mPresenter.toCheckPermission(this)

    }

    override fun showCheckPermissionResult(boolean: Boolean) {
        if (boolean) {
            toast("APP有权限")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
