package com.zzdc.workpolicy

import com.qxj.commondata.content.ConfigPreference

object Config {

    var userId by ConfigPreference("owner", "")
}