package com.qxj.conclusion.dagger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.qxj.commonsdk.dagger.Cooking
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.activity_dagger.*
import javax.inject.Inject

class FactoryActivity : AppCompatActivity() {
    private val TAG = FactoryActivity::class.java.simpleName

    lateinit var chef: Cooking
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger)
//
//        DaggerFactoryActivityComponent
//                .builder()
//                .cookModules(CookModules())
//                .build()
//                .inject(this)
//        Log.d(TAG, "点菜：${chef.cook()}")
//        cook.text = chef.cook()
    }
}
