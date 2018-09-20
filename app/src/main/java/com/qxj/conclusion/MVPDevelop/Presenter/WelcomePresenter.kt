package com.qxj.conclusion.MVPDevelop.Presenter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.qxj.conclusion.AppConfig
import com.qxj.conclusion.MVPDevelop.MVP.IPresenter
import com.qxj.conclusion.MVPDevelop.View.LoginActivity
import com.qxj.conclusion.MVPDevelop.View.MainActivity
import com.qxj.conclusion.MVPDevelop.View.WelcomeActivity
import com.qxj.conclusion.R

class WelcomePresenter(view: WelcomeContract.IWelcomeView) : WelcomeContract.IWelcomePresenter, IPresenter<WelcomeContract.IWelcomeView>(view) {

    private val TAG = WelcomePresenter::class.java.name

    var pageData: IntArray? = null

    override fun checkWelcomeStatus() {
        //请求导航页数据数据
        if (!AppConfig.welcome) {
            pageData = intArrayOf(R.mipmap.we_nv_01, R.mipmap.we_nv_02, R.mipmap.we_nv_03)
            if (pageData != null && pageData!!.isNotEmpty()) {
                mView.get()?.showWelcomePage(pageData!!)
            }
        } else {
            pageData = intArrayOf(R.mipmap.we_st_01)
            if (pageData != null && pageData!!.isNotEmpty()) {
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

    override fun checkTargetActivity() {
        if (AppConfig.userName != "Name-unKnown") {
            mView.get()?.toTargetActivity(MainActivity::class.java)
        } else {
            mView.get()?.toTargetActivity(LoginActivity::class.java)
        }
    }

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
            return  imageView
        }

    }
}