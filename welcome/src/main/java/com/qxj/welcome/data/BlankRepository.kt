package com.qxj.welcome.data

import com.qxj.commonbase.data.Listing
import com.qxj.commonbase.mvvm.Repository

class BlankRepository : Repository {
    override fun <T> getDataList(pageSize: Int): Listing<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}