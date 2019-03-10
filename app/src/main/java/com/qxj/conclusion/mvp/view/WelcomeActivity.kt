package com.qxj.conclusion.mvp.view

import android.app.Activity
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.qxj.commonbase.mvpbase.BaseActivity
import com.qxj.conclusion.mvp.presenter.WelcomeContract
import com.qxj.conclusion.mvp.presenter.WelcomePresenter
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.welcome_navigation_page.*
import kotlinx.android.synthetic.main.welcome_start_page.*
import org.jetbrains.anko.toast

class WelcomeActivity : BaseActivity(), WelcomeContract.IWelcomeView {

    private val TAG = WelcomeActivity::class.java.name

    override fun getLayoutId(): Int = R.layout.activity_welcome


    override val mPresenter: WelcomePresenter = WelcomePresenter(this)

    override fun initView() {
        mPresenter.checkWelcomeStatus()
    }

    override fun <T: PagerAdapter> showWelcomePage(adapter: T) {

        welcome_page.layoutResource = R.layout.welcome_navigation_page
        welcome_page.inflate()

        navigation_welcome.adapter = adapter
        navigation_welcome.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mPresenter.checkWelcomeStatus(position)
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    override fun showEndWelcomeBtn() {
        end_welcome.visibility = View.VISIBLE

        end_welcome.setOnClickListener {
            mPresenter.changeWelcomeStatus(true)
            mPresenter.checkWelcomeStatus()
        }
    }

    override fun showStartPage(images: IntArray) {
        //设置广告
        start_page.layoutResource = R.layout.welcome_start_page
        start_page.inflate()
        start_welcome.setBackgroundResource(images[0])

        close_welcome.visibility = View.VISIBLE
        close_welcome.setOnClickListener {
            //跳过广告
            mPresenter.checkTargetActivity()
        }

    }

    override fun showPermissionMsgDialog(msg: String) {
//        this.showLoading("权限申请", msg, null, "确定", false, null, {
//            mPresenter.toCheckPermission(this)
//        })
    }

    override fun showCheckPermissionResult(boolean: Boolean) {
        if (boolean) {
            toast("APP有权限")
        } else {
            finish()
        }
    }

    override fun <T : Activity> toTargetActivity(cls: Class<T>) {
        val intent = Intent()
        intent.setClass(applicationContext, cls)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
