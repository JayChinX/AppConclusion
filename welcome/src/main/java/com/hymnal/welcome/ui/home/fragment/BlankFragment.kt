package com.hymnal.welcome.ui.home.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.facade.annotation.Route
import com.hymnal.welcome.R
import com.hymnal.welcome.base.BaseFragment
import com.hymnal.welcome.utilities.InjectorUtils
import kotlinx.android.synthetic.main.blank_fragment.*

@Route(path = "/home/fragment/BlankFragment")
class BlankFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.blank_fragment

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        val factory = InjectorUtils.provideBlankViewModelFactory()
        ViewModelProviders.of(this, factory)
                .get(BlankViewModel::class.java)
    }

    override fun subscribeUi() {
        viewModel.data.observe(viewLifecycleOwner, Observer {
            name_blank.text = it
        })

        start.setOnClickListener {

//            viewModel.get()
            dynamic.loading()
            dynamic.setSpeed(82)
        }

        dynamic.setSpeed(50)

    }

}
