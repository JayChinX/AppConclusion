package com.qxj.conclusion.MVPDevelop.Presenter

import com.qxj.conclusion.AppConclusionActivity
import com.qxj.conclusion.MVPDevelop.View.IView
import java.text.FieldPosition

interface WelcomeContract {

    interface IWelcomeView : IView {
        fun showWelcomePage(images: IntArray)
        fun showEndWelcomeBtn()
        fun showStartPage(images: IntArray)
        fun <T: AppConclusionActivity>toTargetActivity(cls:Class<T> )
    }

    interface IWelcomePresenter{
        fun checkWelcomeStatus()
        fun checkWelcomeStatus(position: Int)
        fun changeWelcomeStatus(boolean: Boolean)
        fun checkTargetActivity()
    }
}