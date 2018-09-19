package com.qxj.conclusion.MVPDevelop.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.qxj.conclusion.MVPDevelop.MVP.BaseActivity
import com.qxj.conclusion.MVPDevelop.MVP.IPresenter
import com.qxj.conclusion.MVPDevelop.Presenter.WelcomeContract
import com.qxj.conclusion.MVPDevelop.Presenter.WelcomePresenter
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.welcome_page.*

class WelcomeActivity : BaseActivity(), WelcomeContract.IWelcomeView {


    override fun getLayoutId(): Int = R.layout.activity_welcome

    override val mPresenter: WelcomePresenter = WelcomePresenter(this)

    override fun initView() {
        mPresenter.changeWelcomeStatus(welcome)
    }

    override fun startCheckPermission() {

    }

    override fun showWelcomePage(boolean: Boolean) {

        if (boolean) {
            welcome_page.layoutResource = R.layout.welcome_page
            welcome_page.inflate()

            welcome_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {

                }

            })
        }
    }


}
