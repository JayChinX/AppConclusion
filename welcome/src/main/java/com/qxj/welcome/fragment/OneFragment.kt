package com.qxj.welcome.fragment

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.facade.annotation.Route
import com.qxj.welcome.base.BaseFragment
import com.qxj.welcome.R
import com.qxj.welcome.data.PagedAdapter
import com.qxj.commonbase.data.Resource.Status.*
import com.qxj.welcome.utilities.InjectorUtils
import com.qxj.welcome.viewmodels.OneViewModel
import kotlinx.android.synthetic.main.fragment_one.*
import org.jetbrains.anko.toast

@Route(path = "/home/fragment/OneFragment", name = "One")
class OneFragment : BaseFragment() {

    private val TAG = OneFragment::class.java.simpleName

    override fun getLayoutId()= R.layout.fragment_one

    override fun initView() {

    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        val factory = InjectorUtils.provideOneViewModelFactory()
        ViewModelProviders.of(this, factory)
                .get(OneViewModel::class.java)
    }

    override fun subscribeUi() {
        val adapter = PagedAdapter()
        recycler_view.adapter = adapter
        viewModel.posts.observe(this, Observer(adapter::submitList))

        initSwipeToRefresh()

        viewModel.showData(TAG)
    }

    private fun initSwipeToRefresh() {
        swipe_refresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW)
        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshState.observe(viewLifecycleOwner, Observer {
            if (it == null || viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.STARTED) return@Observer
            Log.d(TAG, "刷新状态变化 ${it.status}")
            when(it.status) {
                SUCCESS -> {
                    swipe_refresh.isRefreshing = false
                    showToast("刷新成功")
                }
                ERROR -> {
                    swipe_refresh.isRefreshing = false
                    showToast("刷新失败")
                }
                LOADING -> {
                    swipe_refresh.isRefreshing = true
                    showToast("刷新中")
                }
            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == null || viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.STARTED) return@Observer
            Log.d(TAG, "网络状态变化 ${it.status}")
            when(it.status) {
                SUCCESS -> Log.d(TAG, "数据加载成功")
                ERROR -> showToast(it.message!!)
                LOADING -> Log.d(TAG, "数据加载中")
            }
        })
    }

    private fun showToast(msg: String) {
        activity!!.toast(msg)
    }

}
