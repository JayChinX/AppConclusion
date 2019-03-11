package com.zzdc.emulationcard

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class HCEService : HostApduService() {
    private val TAG = HCEService::class.java.simpleName
    var messageCounter = 0

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "Deactivated: $reason")

    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        return if (selectAidApdu(commandApdu)) {
            Log.i(TAG, "Application selected")
            getWelcomeMessage()
        }
        else {
            Log.i(TAG, "Received: " +  String(commandApdu))
            getNextMessage()
        }
    }

    private  fun getWelcomeMessage(): ByteArray {
        return "Hello Desktop!".toByteArray()
    }

    private fun getNextMessage(): ByteArray {
        return ("Message from android: " + messageCounter++).toByteArray()
    }

    private fun selectAidApdu(apdu: ByteArray): Boolean {
        return apdu.size >= 2 && apdu[0] == 0.toByte() && apdu[1] == (0xa4).toByte()
    }
}