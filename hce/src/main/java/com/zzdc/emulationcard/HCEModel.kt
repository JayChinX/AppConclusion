package com.zzdc.emulationcard

import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log
import java.io.IOException
import java.util.*
import kotlin.experimental.and

class HCEModel {

    private val TAG = HCEModel::class.java.simpleName

    private fun tagDiscovered(tag: Tag?): String? {
        Log.i(TAG, "New tag discovered")
        // Android's Host-based Card Emulation (HCE) feature implements the
        // ISO-DEP (ISO 14443-4)
        // protocol.
        //
        // In order to communicate with a device using HCE, the discovered tag
        // should be processed
        // using the IsoDep class.
        val isoDep = IsoDep.get(tag)
        if (isoDep != null) {
            try {
                // Connect to the remote HCE device
                isoDep.connect();
                // Build SELECT AID command for our loyalty card service.
                // This command tells the remote device which service we wish to
                // communicate with.
                Log.i(TAG, "Requesting remote AID: ${CardReader.SAMPLE_LOYALTY_CARD_AID}")
                val command = buildSelectApdu(CardReader.SAMPLE_LOYALTY_CARD_AID)
                // Send command to remote device
                Log.i(TAG, "Sending: " + byteArrayToHexString(command))
                val result = isoDep.transceive(command)
                // If AID is successfully selected, 0x9000 is returned as the
                // status word (last 2
                // bytes of the result) by convention. Everything before the
                // status word is
                // optional payload, which is used here to hold the account
                // number.
                val resultLength = result.size
                val statusWord = byteArrayOf(
                        result[resultLength - 2],
                        result[resultLength - 1]
                )
                val payload = result.copyOf(resultLength - 2)
                if (Arrays.equals(CardReader.SELECT_OK_SW, statusWord)) {
                    // The remote HCE device will immediately respond with its
                    // stored account number
                    val accountNumber = String(payload, Charsets.UTF_8)
                    Log.i(TAG, "Received: $accountNumber")
                    // Inform CardReaderFragment of received account number
                    return accountNumber
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error communicating with card: $e");
            }
        }
        return null
    }

    private fun byteArrayToHexString(bytes: ByteArray): String {

        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        val hexChars = CharArray(bytes.size * 2)
        var v = 0
        for (i in 0..bytes.size) {
            v = (bytes[i] and 0xFF.toByte()).toInt()
            hexChars[i * 2] = hexArray[v ushr 4]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)

    }

    private fun buildSelectApdu(aid: String): ByteArray {
        return hexStringToByteArray(CardReader.SELECT_APDU_HEADER + String.format("%02X", aid.length / 2) + aid)
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        for (index in 0..len step 2) {
            data[index / 2] = (Character.digit(s[index], 16) shl 4).toByte()
            +Character.digit(s[index + 1], 16)
        }
        return data
    }

    fun load(tag: Tag): String? {
        // 从Parcelable筛选出各类NFC标准数据
        return tagDiscovered(tag)
    }
}