package com.qxj.conclusion.MVPDevelop.View

import android.content.Intent
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.qxj.conclusion.AppConclusionActivity
import com.qxj.conclusion.MVPDevelop.MVP.BaseActivity
import com.qxj.conclusion.MVPDevelop.Presenter.WelcomeContract
import com.qxj.conclusion.MVPDevelop.Presenter.WelcomePresenter
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.welcome_navigation_page.*
import kotlinx.android.synthetic.main.welcome_start_page.*

class WelcomeActivity : BaseActivity(), WelcomeContract.IWelcomeView {

    private val TAG = WelcomeActivity::class.java.name

    override fun getLayoutId(): Int = R.layout.activity_welcome


    override val mPresenter: WelcomePresenter = WelcomePresenter(this)

    override fun initView() {
        mPresenter.checkWelcomeStatus()
    }
    override fun startCheckPermission() {
        Log.d(TAG, "这里不需要检查权限")
    }

    override fun showWelcomePage(images: IntArray) {

        welcome_page.layoutResource = R.layout.welcome_navigation_page
        welcome_page.inflate()

        navigation_welcome.adapter = WelcomePresenter.NavigationPagerAdapter(images)
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

    override fun <T : AppConclusionActivity> toTargetActivity(cls: Class<T>) {
        val intent = Intent()
        intent.setClass(applicationContext, cls)
        startActivity(intent)
        finish()
    }

}
