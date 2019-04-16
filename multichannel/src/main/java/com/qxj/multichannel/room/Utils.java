package com.qxj.multichannel.room;

import com.qxj.commondata.CommonDataApp;

public class Utils {

    public void test() {
        UserDatabase.getInstance(CommonDataApp.Companion.getINSTANCE()).getUserDao().getAll();

    }
}
