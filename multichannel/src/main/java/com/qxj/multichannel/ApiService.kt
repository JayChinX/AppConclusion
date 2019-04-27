package com.qxj.multichannel

import com.qxj.commonbase.network.NetworkManger

object ApiService : MuApi by NetworkManger.getInstance().getApiService() {
}