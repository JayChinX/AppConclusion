package com.qxj.conclusion.MVPDevelop.Presenter

import com.qxj.conclusion.MVPDevelop.View.IView

interface WelcomeContract {

    interface IWelcomeView : IView {
        fun showWelcomePage(boolean: Boolean)
    }

    interface IWelcomePresenter{
        fun changeWelcomeStatus(boolean: Boolean)
    }
}