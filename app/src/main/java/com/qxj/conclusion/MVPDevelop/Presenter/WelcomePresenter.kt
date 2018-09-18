package com.qxj.conclusion.MVPDevelop.Presenter

import com.qxj.conclusion.MVPDevelop.MVP.IPresenter

class WelcomePresenter(view: WelcomeContract.IWelcomeView) : WelcomeContract.IWelcomePresenter, IPresenter<WelcomeContract.IWelcomeView>(view) {


    override fun changeWelcomeStatus(boolean: Boolean) {
        mView.get()?.showWelcomePage(boolean)
    }
}