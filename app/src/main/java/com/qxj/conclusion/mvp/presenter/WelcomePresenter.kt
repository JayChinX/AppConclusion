package com.qxj.conclusion.mvp.presenter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.qxj.conclusion.AppConclusionActivity
import com.qxj.conclusion.AppConfig
import com.qxj.conclusion.util.PermissionUtil
import com.qxj.conclusion.mvp.base.IPresenter
import com.qxj.conclusion.mvp.model.UserModel
import com.qxj.conclusion.mvp.view.LoginActivity
import com.qxj.conclusion.mvp.view.MainActivity
import com.qxj.conclusion.R

class WelcomePresenter(view: WelcomeContract.IWelcomeView) : WelcomeContract.IWelcomePresenter, IPresenter<WelcomeContract.IWelcomeView>(view) {

    private val TAG = WelcomePresenter::class.java.name

    var pageData: IntArray? = null

    var permissionUtil: PermissionUtil? = null

    var msg: String = """
        该应用需要存储权限，用
        于存储图片等信息
    """.trimIndent()

    override fun initLifecycle(owner: LifecycleOwner) {

    }

    override fun checkWelcomeStatus() {
        //请求导航页数据数据
        if (!AppConfig.welcome) {
            pageData = intArrayOf(R.mipmap.we_nv_01, R.mipmap.we_nv_02, R.mipmap.we_nv_03)
            if (pageData != null && pageData!!.isNotEmpty()) {
                mView.get()?.showWelcomePage(adapter = NavigationPagerAdapter(pageData!!))
            }
        } else {
            pageData = intArrayOf(R.mipmap.we_st_01)
            if (pageData != null && pageData!!.isNotEmpty()) {
                mView.get()?.showPermissionMsgDialog(msg)
                mView.get()?.showStartPage(pageData!!)
            }
        }

    }

    override fun changeWelcomeStatus(boolean: Boolean) {
        AppConfig.welcome = boolean

    }

    override fun checkWelcomeStatus(position: Int) {
        if (position == pageData!!.size - 1) {
            mView.get()?.showEndWelcomeBtn()
        }
    }

    override fun toCheckPermission(context: Context) {
        permissionUtil = PermissionUtil(context as AppConclusionActivity)
        UserModel.toCheckPermission(permissionUtil!!) {
            mView.get()?.showCheckPermissionResult(it)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionUtil!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun checkTargetActivity() {
        if (AppConfig.userName != "Name-unKnown") {
            mView.get()?.toTargetActivity(cls = MainActivity::class.java)
        } else {
            mView.get()?.toTargetActivity(cls = LoginActivity::class.java)
        }
    }


    //viewPager适配器
    class NavigationPagerAdapter(var data: IntArray) : PagerAdapter() {

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            super.destroyItem(container, position, `object`)
            container.removeView(`object` as View?)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            return super.instantiateItem(container, position)
            var i = data[position]
            var imageView = ImageView(container.context)
            imageView.setBackgroundResource(i)
            container.addView(imageView)
            return imageView
        }

    }
}