package com.qxj.conclusion.MVPDevelop.View

import com.qxj.conclusion.ConclusionUtils.PermissionUtil
import com.qxj.conclusion.MVPDevelop.MVP.BaseActivity
import com.qxj.conclusion.MVPDevelop.Presenter.PermissionContract
import com.qxj.conclusion.MVPDevelop.Presenter.PermissionPresenter
import com.qxj.conclusion.R
import org.jetbrains.anko.toast

class PermissionActivity : BaseActivity(), PermissionContract.IPermissionView {

    private val TAG: String = PermissionActivity::class.java.name

    private val permissionUtil: PermissionUtil = PermissionUtil(this)

    override val mPresenter: PermissionPresenter = PermissionPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_permission

    override fun initView() {

        val id = intent.getIntExtra("id", 0)
        toast("获取id为$id")
    }

    //检查必要权限
    override fun startCheckPermission() {
        mPresenter.toCheckPermission(permissionUtil)
    }


    override fun showCheckPermissionResult(boolean: Boolean) {
        userId
        if (boolean) {
            toast("APP有权限")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
