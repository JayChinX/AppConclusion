package com.qxj.conclusion.api

import com.qxj.commonbase.network.NetworkManger

object ApiService : ConApi by NetworkManger.getInstance().getApiService()