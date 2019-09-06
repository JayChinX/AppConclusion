package com.hymnal.welcome.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hymnal.welcome.utilities.checkPermission
import com.hymnal.welcome.utilities.permissionsResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.hymnal.welcome.R
import com.hymnal.welcome.base.BaseActivity
import com.hymnal.welcome.ui.home.data.model.Garden
import com.hymnal.welcome.utilities.InjectorUtils
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.floating_button.*
import kotlinx.android.synthetic.main.home_app_bar.*

@Route(path = "/home/activity/HomeActivity", group = "home")
class HomeActivity : BaseActivity() {

    private val TAG = HomeActivity::class.java.simpleName

    //侧滑菜单
    private val onNavigationItemSelectedListener = NavigationView
        .OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_camera -> {
                    // Handle the camera action
                }
                R.id.nav_gallery -> {

                }
                R.id.nav_slideshow -> {

                }
                R.id.nav_manage -> {

                }
                R.id.nav_share -> {

                }
                R.id.nav_send -> {

                }
            }
//                drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

    //底部导航
    private val mOnNavigationItemSelectedListener = BottomNavigationView
        .OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    pagerChange(0)
                    true
                }
                R.id.navigation_dashboard -> {
                    pagerChange(1)
                    true
                }
                R.id.navigation_notifications -> {
                    pagerChange(2)
                    true
                }
                R.id.navigation_find -> {
                    pagerChange(3)
                    true
                }
                else -> false
            }
        }

    private val mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            pagerChange(position)
        }

    }

    override fun getLayoutId(): Int = R.layout.home_activity


    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        val factory = InjectorUtils.provideHomeViewModelFactory()
        ViewModelProviders.of(this, factory)
            .get(HomeViewModel::class.java)
    }

    override fun subscribeUi() {
//        isFullScreen()
//        setStatusBarColor()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawer_layout.let {
            val toggle = ActionBarDrawerToggle(
                this, it, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            it.addDrawerListener(toggle)
            toggle.syncState()
            //关联drawerLayout后设置有效，在xml或之前设置无效
//            toolbar.setNavigationIcon(R.mipmap.nav_icon_menu)
        }

        checkPermission {

        }

        viewModel.posts.observe(this, Observer {
            viewPager.adapter = FragmentPagerAdapter(
                supportFragmentManager,
                *it.toTypedArray()
            )
        })


        viewModel.title.observe(this, Observer {
            Log.d(TAG, "当前在$it 页面")
            title_name.text = it
        })

        viewModel.pager.observe(this, Observer { position ->
            if (this.lifecycle.currentState == Lifecycle.State.STARTED) {
                Log.d(TAG, "viewPager pagerItem 默认页 $position")
                viewPager.currentItem = position
                navigation.selectedItemId = navigation.menu.getItem(position).itemId
            } else {
                if (viewPager.currentItem != position) {
                    Log.d(TAG, "viewPager切换到页面$position")
                    viewPager.currentItem = position
                } else {
                    Log.d(TAG, "navigation切换到$position")
                    navigation.menu.getItem(position).isChecked = true
                }
            }

        })

        viewModel.showData()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
//            showDialog("提示", "信息")
            viewModel.sendMsg()
        }
        nav_view.setNavigationItemSelectedListener(onNavigationItemSelectedListener)
        viewPager.addOnPageChangeListener(mOnPageChangeListener)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val textLayout = nav_view.menu.findItem(R.id.nav_share).actionView as LinearLayout
        val text = textLayout.findViewById<TextView>(R.id.text_d)
        text.text = "Hello"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsResult(requestCode, permissions, grantResults)
    }

    private fun pagerChange(position: Int) {
        viewModel.pagerChange(position)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                viewModel.startActivity(R.id.action_settings)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("WrongConstant")
    internal class FragmentPagerAdapter(
        fm: FragmentManager,
        private vararg var fragments: Garden
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragments[position].fragment

        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

        override fun getCount(): Int = fragments.size

    }
}