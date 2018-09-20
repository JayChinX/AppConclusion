package com.qxj.conclusion

import com.qxj.conclusion.ConclusionUtils.ConfigPreference

object AppConfig {

    var welcome by ConfigPreference("welcomeStatus", false)
    var variable by ConfigPreference("wakeupStatus", true)
    var userId by ConfigPreference("userId", "ID-unKnown")
    var userName by ConfigPreference("userName", "Name-unKnown")
    var userAccount by ConfigPreference("account", "A-unKnown")
    var userPassword by ConfigPreference("password", "P-unKnown")
}