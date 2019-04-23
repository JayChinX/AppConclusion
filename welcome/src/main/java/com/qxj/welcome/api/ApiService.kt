package com.qxj.welcome.api

import com.qxj.commonsdk.network.NetworkManger

object ApiService: HttpApi by NetworkManger.getInstance(TokenInterceptor()).getApiService()