package com.hymnal.welcome.kodein

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.hymnal.welcome.R
import com.hymnal.welcome.base.BaseActivity
import com.hymnal.welcome.utilities.InjectorUtils
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

class SampleActivity : BaseActivity(), KodeinAware {


    override fun getLayoutId(): Int = R.layout.activity_sample

    //1
    //closestKodein() 返回相邻上层的一个Kodein容器，
    //对于Activity来说返回的是Application层级的Kodein容器
    private val parentKodein by closestKodein()

    override val kodein: Kodein by retainedKodein {
        //2
        //将Application层级的Kodein容器放在Activity的kodein容器中
        extend(parentKodein, copy = Copy.All)
        //3
        //注入mainKodeinModule
        import(sampleKodeinModel)
        //4
        bind<SampleActivity>() with  instance(this@SampleActivity)
    }

    // 注入 MainNavigator 控制的Activity的视图导航
//    private val navigator: MainNavigator by instance()
    // 注入MainViewModel管理的业务数据
    private val sampleViewModel: SampleViewModel by instance()

    fun<T: ViewModel> viewModel( modelClass: Class<T>) : T{
        val factory = InjectorUtils.provideHomeViewModelFactory()
        return ViewModelProviders.of(this, factory)
            .get(modelClass)
    }

    override fun subscribeUi() {

    }


}
