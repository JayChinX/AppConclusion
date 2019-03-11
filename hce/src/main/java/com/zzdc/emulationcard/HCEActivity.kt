package com.zzdc.emulationcard

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import android.util.Log
import android.view.View
import com.qxj.commonbase.mvvm.BaseActivity
import com.zzdc.emulationcard.databinding.ActivityHceBinding


class HCEActivity : BaseActivity<ActivityHceBinding>() {

    private val TAG = HCEActivity::class.java.simpleName

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private var version: Int = 0

    lateinit var vm: HCEViewModel

    override fun getLayoutId(): Int = R.layout.activity_hce

    override fun initView() {
        vm = HCEViewModel(this)
        binding.let {
            it.hce= vm.hce
            it.vm = vm
        }
    }

    override fun initData() {
        vm.checkHEC(this)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(this,
                0,
                Intent(this, HCEActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0)
        version = Integer.parseInt(Build.VERSION.SDK)
        if (version < 19) {
            onNewIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val p = intent!!.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        vm.read(p)

    }

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, CardReader.FILTERS, CardReader.TECHLIST)
        enableReaderMode()
        Log.e(TAG, "onResume ${IsoDep::class.java.name}")
        refreshStatus()
    }

    private fun enableReaderMode() {
        if (version < 19) return
        val FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
        nfcAdapter.enableReaderMode(this, {
            runOnUiThread {
                vm.read(it)
            }
        }, FLAGS, null)
    }

    private fun refreshStatus() {
        val tip = when {
            nfcAdapter == null -> this.resources.getString(R.string.tip_nfc_notfound)
            nfcAdapter.isEnabled -> this.resources.getString(R.string.tip_nfc_enabled)
            else -> this.resources.getString(R.string.tip_nfc_disabled)
        }

        val s = StringBuilder(
                this.resources.getString(R.string.app_name))

        s.append("  --  ").append(tip)
        title = s
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
        disableReaderMode()
    }

    private fun disableReaderMode() {

        if (version < 19)
            return
        nfcAdapter.disableReaderMode(this)

    }


    override fun onClick(v: View?) {
    }


}
