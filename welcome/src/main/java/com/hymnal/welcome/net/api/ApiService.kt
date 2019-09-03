package com.hymnal.welcome.net.api

import com.hymnal.base.network.NetworkManger

object ApiService: WelcomeApi by NetworkManger.getInstance().getApiService()