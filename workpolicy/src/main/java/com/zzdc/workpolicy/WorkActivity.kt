package com.zzdc.workpolicy

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.UserManager
import android.support.v4.os.BuildCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_work.*


class WorkActivity : AppCompatActivity() {

    private val TAG = WorkActivity::class.java.simpleName
    private val GOOGLE_ACCOUNT_TYPE = "com.google"

    private var mDisallowModifyAccounts: Boolean = false
    val EXTRA_NEXT_ACTIVITY_INTENT = "nextActivityIntent"
    private lateinit var mUserManager: UserManager
    private lateinit var manager: DevicePolicyManager
    internal lateinit var componentName: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pm = packageManager
        if (!pm.hasSystemFeature(PackageManager.FEATURE_MANAGED_USERS)) {

            // This device does not support native managed profiles!

        }
        //多用户管理
        mUserManager = getSystemService(Context.USER_SERVICE) as UserManager
        //策略管理
        manager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        //owner权限
        componentName = DeviceAdminReceiver.getComponentName(this) as ComponentName
        setContentView(R.layout.activity_work)

        PackageManager.GET_DISABLED_COMPONENTS
        PackageManager.GET_UNINSTALLED_PACKAGES

        start.setOnClickListener {
            createProfile()
        }
        profile.setOnClickListener {
            startActivity(Intent(this, FinalizeActivity::class.java))
            finish()
        }
    }


    private fun disableUserRestrictions() {
        //用户数量限制
        if (BuildCompat.isAtLeastN()) {
            // DPC is allowed to bypass DISALLOW_MODIFY_ACCOUNTS on N or above.
            Log.v(TAG, "skip disabling user restriction on N or above")
            return
        }
        Log.v(TAG, "disabling user restrictions")
        //清楚用户限制
        mDisallowModifyAccounts = mUserManager.hasUserRestriction(UserManager.DISALLOW_MODIFY_ACCOUNTS)
        manager
                .clearUserRestriction(componentName, UserManager.DISALLOW_MODIFY_ACCOUNTS)
    }


    private fun restoreUserRestrictions() {
        if (BuildCompat.isAtLeastN()) {
            // DPC is allowed to bypass DISALLOW_MODIFY_ACCOUNTS on N or above.
            Log.v(TAG, "skip restoring user restrictions on N or above")
            return
        }
        //恢复限制
        Log.v(TAG, "restoring user restrictions")
        if (mDisallowModifyAccounts) {
            manager
                    .addUserRestriction(componentName, UserManager.DISALLOW_MODIFY_ACCOUNTS)
        }
    }

    private val REQUEST_PROVISION_MANAGED_PROFILE = 1

    private fun createProfile() {

// You'll need the package name for the WPC app.
        val myWPCPackageName = "com.zzdc.workpolicy"

// Set up the provisioning intent
        val provisioningIntent = Intent("android.app.action.PROVISION_MANAGED_PROFILE")
        provisioningIntent.putExtra(myWPCPackageName,
                this.applicationContext.packageName)

        if (provisioningIntent.resolveActivity(this.packageManager) == null) {

            // No handler for intent! Can't provision this device.
            // Show an error message and cancel.
            Log.d(TAG, "aaaa")
        } else {
            Log.d(TAG, "bbbb")
            // REQUEST_PROVISION_MANAGED_PROFILE is defined
            // to be a suitable request code
            startActivityForResult(provisioningIntent,
                    REQUEST_PROVISION_MANAGED_PROFILE)
            this.finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Check if this is the result of the provisioning activity
        if (requestCode == REQUEST_PROVISION_MANAGED_PROFILE) {

            // If provisioning was successful, the result code is
            // Activity.RESULT_OK
            if (resultCode == Activity.RESULT_OK) {
                // Hurray! Managed profile created and provisioned!

            } else {
                // Boo! Provisioning failed!
            }
            return

        } else {
            // This is the result of some other activity, call the superclass
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        disableUserRestrictions()
    }

    override fun onPause() {
        super.onPause()
        restoreUserRestrictions()
    }

    companion object {
        lateinit var EXTRA_NEXT_ACTIVITY_INTENT: String
    }

}
