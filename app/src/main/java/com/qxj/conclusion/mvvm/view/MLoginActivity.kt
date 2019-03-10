package com.qxj.conclusion.mvvm.view

import android.databinding.Observable
import android.util.Log
import android.view.View
import com.qxj.commonbase.mvvm.BaseActivity
import com.qxj.conclusion.R
import com.qxj.conclusion.databinding.ActivityMloginBinding
import com.qxj.conclusion.mvvm.viewmodel.VMLoginViewModel
import org.jetbrains.anko.toast

class MLoginActivity : BaseActivity<ActivityMloginBinding>() {

    private val TAG = MLoginActivity::class.java.simpleName

    lateinit var vm: VMLoginViewModel

    override fun initView() {
        vm = VMLoginViewModel(this)
        binding.let {
            it.vm = vm
            it.user = vm.user
            it.presenter = this
        }
    }

    override fun initData() {
        //对数据源进行监听
        vm.loading.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (vm.loading.get()) {
                    Log.d(TAG, "开始")
                } else {
                    Log.d(TAG, "结束")
                }
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.activity_mlogin


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start -> {
                toast("开始")
                vm.getUser()
            }
        }
    }
}
