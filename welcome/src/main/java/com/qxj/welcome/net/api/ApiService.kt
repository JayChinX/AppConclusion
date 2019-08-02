package com.qxj.welcome.net.api

import com.qxj.commonbase.network.NetworkManger

object ApiService: WelcomeApi by NetworkManger.getInstance().getApiService()