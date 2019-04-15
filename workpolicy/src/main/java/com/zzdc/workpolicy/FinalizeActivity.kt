/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzdc.workpolicy

import android.accounts.AccountManager
import android.app.Activity
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.os.BuildCompat
import android.view.View
import com.qxj.commonsdk.provider.FileProvider8
import com.zzdc.workpolicy.Config.userId
import kotlinx.android.synthetic.main.activity_finalize.*
import org.jetbrains.anko.toast
import java.io.File


class FinalizeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * 启动策略
         */
        if (savedInstanceState == null) {
            if (isManagedProfileOwner(this)) {
                enableProfile(this)
            }
        }

        setContentView(R.layout.activity_finalize)

        start.setOnClickListener {
            this.onNavigateNext(it)

        }

        install.setOnClickListener {
            toast("userId $userId")
        }


        /**
         * 已经创建了工作空间
         */
        // This is just a user friendly shortcut to the policy management screen of this app.
//        try {
//            val packageManager = packageManager
//            val applicationInfo = packageManager.getApplicationInfo(
//                    packageName, 0 /* Default flags */)
//            app_icon.setImageDrawable(packageManager.getApplicationIcon(applicationInfo))
//            app_label.text = packageManager.getApplicationLabel(applicationInfo)
//        } catch (e: PackageManager.NameNotFoundException) {
//            Log.w("TestDPC", "Couldn't look up our own package?!?!", e)
//        }

        // Show the user which account now has management, if specified.
//        val addedAccount = intent.getStringExtra(LaunchIntentUtil.EXTRA_ACCOUNT_NAME)
//        if (addedAccount != null) {
//            val accountMigrationStatusLayout: View
//            if (isAccountMigrated(addedAccount)) {
//                accountMigrationStatusLayout = findViewById(R.id.account_migration_success)
//            } else {
//                accountMigrationStatusLayout = findViewById(R.id.account_migration_fail)
//            }
//            accountMigrationStatusLayout.visibility = View.VISIBLE
//            val managedAccountName = accountMigrationStatusLayout.findViewById(
//                    R.id.managed_account_name) as TextView
//            managedAccountName.text = addedAccount
//        }
    }

    private fun isAccountMigrated(addedAccount: String): Boolean {
        val accounts = AccountManager.get(this).accounts
        for (account in accounts) {
            if (addedAccount.equals(account.name, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    private fun onNavigateNext(nextButton: View) {
        finish()
    }

    private fun isManagedProfileOwner(context: Context): Boolean {
        val dpm = getDevicePolicyManager(context)

        if (BuildCompat.isAtLeastN()) {
            return try {
                dpm.isManagedProfile(DeviceAdminReceiver.getComponentName(context))
            } catch (ex: SecurityException) {
                // This is thrown if we are neither profile owner nor device owner.
                false
            }

        }

        // Pre-N, TestDPC only supports being the profile owner for a managed profile. Other apps
        // may support being a profile owner in other contexts (e.g. a secondary user) which will
        // require further checks.
        return isProfileOwner(context)
    }

    private fun getDevicePolicyManager(context: Context): DevicePolicyManager {
        return context.getSystemService(Service.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private fun isProfileOwner(context: Context): Boolean {
        val dpm = getDevicePolicyManager(context)
        return dpm.isProfileOwnerApp(context.packageName)
    }

    //启用策略  开始创建work app 和fild
    private fun enableProfile(context: Context) {
        val manager = context.getSystemService(
                Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = DeviceAdminReceiver.getComponentName(context)
        // This is the name for the newly created managed profile.
        manager.setProfileName(componentName, "Sample Managed Profile")
        // We enable the profile here.
        manager.setProfileEnabled(componentName)
    }

    private fun installApk(view: View) {
        val file = File(Environment.getExternalStorageDirectory(),
                "arouter-debug.apk")
        val intent = Intent(Intent.ACTION_VIEW)
        // 仅需改变这一行
        FileProvider8.setIntentDataAndType(this,
                intent, "application/vnd.android.package-archive", file, true)
        startActivity(intent)
    }
}
