package com.qxj.conclusion.MVPDevelop.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.qxj.conclusion.MVPDevelop.MVP.BaseActivity
import com.qxj.conclusion.MVPDevelop.MVP.IPresenter
import com.qxj.conclusion.MVPDevelop.Presenter.WelcomeContract
import com.qxj.conclusion.MVPDevelop.Presenter.WelcomePresenter
import com.qxj.conclusion.R

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

        }
    }


}
