package com.zzdc.emulationcard

import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.*
import android.os.Parcelable
import android.util.Log
import java.io.IOException
import java.util.*
import kotlin.experimental.and

object CardReader {
    private val TAG = CardReader::class.java.simpleName

    // AID for our loyalty card service.
    val SAMPLE_LOYALTY_CARD_AID: String = "F222222222"
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    val SELECT_APDU_HEADER: String = "00A40400"
    // "OK" status word sent in response to SELECT AID command (0x9000)
    val SELECT_OK_SW = byteArrayOf(0x90.toByte(), 0x00.toByte(), 0xa4.toByte())

    val TECHLIST = arrayOf(
            arrayOf(IsoDep::class.java.name),
            arrayOf(NfcA::class.java.name),
            arrayOf(NfcB::class.java.name),
            arrayOf(NfcV::class.java.name),
            arrayOf(NfcF::class.java.name),
            arrayOf(Ndef::class.java.name))
    val FILTERS = arrayOf(
            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))


}