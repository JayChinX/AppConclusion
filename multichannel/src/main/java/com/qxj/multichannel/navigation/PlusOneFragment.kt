package com.qxj.multichannel.navigation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.qxj.commondata.paging.*
import com.qxj.multichannel.R
import kotlinx.android.synthetic.main.fragment_plus_one.*

class PlusOneFragment : Fragment() {

    private val studentViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = ServiceLocator.instance(activity!!.application)
                        .getRepository(StudentRepository.Type.IN_MEMORY_BY_ITEM)
                return StudentViewModel(activity!!.application, repo) as T
            }

        })[StudentViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_plus_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        plus_one_button.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        val studentAdapter = StudentAdapter()
        recycler_view.adapter = studentAdapter
        //观察数据变化
        studentViewModel.posts.observe(this, Observer(studentAdapter::submitList))

        initSwipeToRefresh()
        studentViewModel.showData("")
    }

    private fun initSwipeToRefresh() {
        swipe_refresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW)
        swipe_refresh.setOnRefreshListener {
            studentViewModel.refresh()
        }
        //观察刷新状态变化
        studentViewModel.refreshState.observe(this, Observer {
            if (it == null) return@Observer
            //状态变化
            Log.d("qxj", "刷新状态 ${it.status}")
            when (it.status) {
                Status.SUCCESS -> swipe_refresh.isRefreshing = false
                Status.LOADING -> swipe_refresh.isRefreshing = true
                Status.ERROR -> swipe_refresh.isRefreshing = false
            }
        })
        //观察网络请求
        studentViewModel.networkState.observe(this, Observer {
            if (it == null) return@Observer
            Log.d("qxj", "网络状态 ${it.status}")
        })
    }

}
