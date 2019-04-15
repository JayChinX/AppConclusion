package com.qxj.commondata.room;

import com.qxj.commondata.CommonDataApp;

public class Utils {

    public void test() {
        UserDatabase.getInstance(CommonDataApp.Companion.getInstance()).getUserDao().getAll();

    }
}
