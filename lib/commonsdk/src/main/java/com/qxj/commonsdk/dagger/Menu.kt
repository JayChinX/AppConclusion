package com.qxj.commonsdk.dagger

import javax.inject.Inject

class Menu @Inject constructor(var menus: Map<String, Boolean>)