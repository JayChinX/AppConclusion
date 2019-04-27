package com.qxj.welcome.api

import com.qxj.commonbase.network.NetworkManger

object ApiService: WelcomeApi by NetworkManger.getInstance().getApiService()