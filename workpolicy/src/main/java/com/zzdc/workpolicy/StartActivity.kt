package com.zzdc.workpolicy

import android.app.Activity
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.app.admin.DevicePolicyManager.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.support.v4.os.BuildCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.qxj.commonsdk.provider.FileProvider8
import com.zzdc.workpolicy.Config.userId
import com.zzdc.workpolicy.Util.isManagedProfileOwner
import kotlinx.android.synthetic.main.activity_start.*
import java.io.File

class StartActivity : AppCompatActivity() {

    private val TAG = StartActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        start_set.setOnClickListener {

            startSet()
        }
        install.setOnClickListener {
            installApk(it)
        }

        val owner: String = when {
            Util.isDeviceOwner(this) -> "isDeviceOwner"
            Util.isProfileOwner(this) -> "isProfileOwner"
            isManagedProfileOwner(this) -> "ManagedProfileOwner"
            else -> "no DeviceOwner or ProfileOwner"
        }

        profile_manager.text = owner
        userId = "12345"

    }

    private fun startSet() {
        maybeLaunchProvisioning(1)
    }

    private fun maybeLaunchProvisioning(requestCode: Int) {


        val intent = Intent(ACTION_PROVISION_MANAGED_PROFILE)
        intent.putExtra(EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME,
                DeviceAdminReceiver.getComponentName(this))

        val adminExtras = PersistableBundle()
//        maybeSpecifySyncAuthExtras(intent, adminExtras)
//        specifyDefaultDisclaimers(intent)
        if (adminExtras.size() > 0) {
            intent.putExtra(EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE, adminExtras)
        }
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(intent, requestCode)
        } else {
            Toast.makeText(this, "provisioning_not_supported", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun maybeSpecifySyncAuthExtras(intent: Intent, adminExtras: PersistableBundle) {

        val launchIntent = this.intent

        if (!LaunchIntentUtil.isSynchronousAuthLaunch(launchIntent)) {
            // Don't do anything if this isn't a sync-auth flow.
            return
        }

        val accountToMigrate = LaunchIntentUtil.getAddedAccount(launchIntent)
        if (accountToMigrate != null) {
            // EXTRA_PROVISIONING_ACCOUNT_TO_MIGRATE only supported in API 22+.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                // Configure the account to migrate into the managed profile if setup
                // completes.
                intent.putExtra(EXTRA_PROVISIONING_ACCOUNT_TO_MIGRATE, accountToMigrate)
            } else {
                Toast.makeText(this, "migration_not_supported", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        // Perculate launch intent extras through to DeviceAdminReceiver so they can be used there.
        LaunchIntentUtil.prepareDeviceAdminExtras(launchIntent, adminExtras)
    }

    private fun specifyDefaultDisclaimers(intent: Intent) {
        if (BuildCompat.isAtLeastO()) {
            val emmBundle = Bundle()
            emmBundle.putString(DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_HEADER,
                    "default_disclaimer_emm_name")
//            emmBundle.putParcelable(DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_CONTENT,
//                    resourceToUri(getActivity(), R.raw.emm_disclaimer))
            val companyBundle = Bundle()
            companyBundle.putString(DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_HEADER,
                    "default_disclaimer_company_name")
//            companyBundle.putParcelable(DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_CONTENT,
//                    resourceToUri(getActivity(), R.raw.company_disclaimer))
            intent.putExtra(DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMERS,
                    arrayOf(emmBundle, companyBundle))
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            1, 2 -> if (resultCode == Activity.RESULT_OK) {
                // Success, finish the enclosing activity. NOTE: Only finish once we're done
                // here, as in synchronous auth cases we don't want the user to return to the
                // Android setup wizard or add-account flow prematurely.
                Log.d(TAG, "Success")
                this.setResult(Activity.RESULT_OK)
                this.finish()
            } else {
                // Something went wrong (either provisioning failed, or the user backed out).
                // Let the user decide how to proceed.
                Toast.makeText(this, "provisioning_failed_or_cancelled",
                        Toast.LENGTH_SHORT).show()
            }
            3 -> if (data != null && data.data != null) {
                Log.d(TAG, ".....")
            }
        }
    }
    private fun getDevicePolicyManager(context: Context): DevicePolicyManager {
        return context.getSystemService(Service.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private fun installApk(view: View) {
        val intent = Intent()
        val dpm = getDevicePolicyManager(this)
        dpm.enableSystemApp(
                DeviceAdminReceiver.getComponentName(this), intent)
//        val file = File(Environment.getExternalStorageDirectory(),
//                "arouter-debug.apk")
//        Log.d(TAG, file.path)
//        val intent = Intent(Intent.ACTION_VIEW)
//        // 仅需改变这一行
//        FileProvider8.setIntentDataAndType(this,
//                intent, "application/vnd.android.package-archive", file, true)
//        startActivity(intent)
    }
}
