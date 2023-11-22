package impacta.contactless.features.activekeys.domain

import android.app.Service
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.google.firebase.installations.Utils
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.util.Arrays


class HCEService : HostApduService() {

    private val TAG = "HCEService"

    private val APDU_SELECT = byteArrayOf(
        0x00.toByte(), // CLA	- Class - Class of instruction
        0xA4.toByte(), // INS	- Instruction - Instruction code
        0x04.toByte(), // P1	- Parameter 1 - Instruction parameter 1
        0x00.toByte(), // P2	- Parameter 2 - Instruction parameter 2
        0x07.toByte(), // Lc field	- Number of bytes present in the data field of the command
        0xD2.toByte(),
        0x76.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x85.toByte(),
        0x01.toByte(),
        0x01.toByte(), // NDEF Tag Application name
        0x00.toByte(), // Le field	- Maximum number of bytes expected in the data field of the response to the command
    )

    private val CAPABILITY_CONTAINER_OK = byteArrayOf(
        0x00.toByte(), // CLA	- Class - Class of instruction
        0xa4.toByte(), // INS	- Instruction - Instruction code
        0x00.toByte(), // P1	- Parameter 1 - Instruction parameter 1
        0x0c.toByte(), // P2	- Parameter 2 - Instruction parameter 2
        0x02.toByte(), // Lc field	- Number of bytes present in the data field of the command
        0xe1.toByte(),
        0x03.toByte(), // file identifier of the CC file
    )

    private val SELECT_NDEF_FILE = byteArrayOf(
        0x00.toByte(),
        0xa4.toByte(),
        0x00.toByte(),
        0x0c.toByte(),
        0x02.toByte(),
        0xE1.toByte(),
        0x04.toByte()
    )

//    private val READ_CAPABILITY_CONTAINER = byteArrayOf(
//        0x00.toByte(), // CLA	- Class - Class of instruction
//        0xb0.toByte(), // INS	- Instruction - Instruction code
//        0x00.toByte(), // P1	- Parameter 1 - Instruction parameter 1
//        0x00.toByte(), // P2	- Parameter 2 - Instruction parameter 2
//        0x0f.toByte(), // Lc field	- Number of bytes present in the data field of the command
//    )

    // In the scenario that we have done a CC read, the same byte[] match
    // for ReadBinary would trigger and we don't want that in succession
    private var READ_CAPABILITY_CONTAINER_CHECK = false

//    private val READ_CAPABILITY_CONTAINER_RESPONSE = byteArrayOf(
//        0x00.toByte(), 0x11.toByte(), // CCLEN length of the CC file
//        0x20.toByte(), // Mapping Version 2.0
//        0xFF.toByte(), 0xFF.toByte(), // MLe maximum
//        0xFF.toByte(), 0xFF.toByte(), // MLc maximum
//        0x04.toByte(), // T field of the NDEF File Control TLV
//        0x06.toByte(), // L field of the NDEF File Control TLV
//        0xE1.toByte(), 0x04.toByte(), // File Identifier of NDEF file
//        0xFF.toByte(), 0xFE.toByte(), // Maximum NDEF file size of 65534 bytes
//        0x00.toByte(), // Read access without any security
//        0xFF.toByte(), // Write access without any security
//        0x90.toByte(), 0x00.toByte(), // A_OKAY
//    )

    private val NDEF_SELECT_OK = byteArrayOf(
        0x00.toByte(), // CLA	- Class - Class of instruction
        0xa4.toByte(), // Instruction byte (INS) for Select command
        0x00.toByte(), // Parameter byte (P1), select by identifier
        0x0c.toByte(), // Parameter byte (P1), select by identifier
        0x02.toByte(), // Lc field	- Number of bytes present in the data field of the command
        0xE1.toByte(),
        0x04.toByte(), // file identifier of the NDEF file retrieved from the CC file
    )

    private val NDEF_READ_BINARY = byteArrayOf(
        0x00.toByte(), // Class byte (CLA)
        0xb0.toByte(), // Instruction byte (INS) for ReadBinary command
    )

    private val NDEF_READ_BINARY_NLEN = byteArrayOf(
        0x00.toByte(), // Class byte (CLA)
        0xb0.toByte(), // Instruction byte (INS) for ReadBinary command
        0x00.toByte(),
        0x00.toByte(), // Parameter byte (P1, P2), offset inside the CC file
        0x02.toByte(), // Le field
    )

    private val CAPABILITY_CONTAINER_FILE = byteArrayOf(
        0x00,
        0x0f,  // CCLEN
        0x20,  // Mapping Version
        0x00,
        0x3b,  // Maximum R-APDU data size
        0x00,
        0x34,  // Maximum C-APDU data size
        0x04,
        0x06,
        0xe1.toByte(),
        0x04,
        0x00.toByte(),
        0xff.toByte(),  // Maximum NDEF size, do NOT extend this value
        0x00,
        0xff.toByte()
    )


    private val A_OKAY = byteArrayOf(
        0x90.toByte(), // SW1	Status byte 1 - Command processing status
        0x00.toByte(), // SW2	Status byte 2 - Command processing qualifier
    )

    private val A_ERROR = byteArrayOf(
        0x6A.toByte(), // SW1	Status byte 1 - Command processing status
        0x82.toByte(), // SW2	Status byte 2 - Command processing qualifier
    )

    private var KEY_MSG = ""

    private val NDEF_ID = byteArrayOf(0xE1.toByte(), 0x04.toByte())

    private var NDEF_URI = NdefMessage(createTextRecord("en", "Ciao, come va?", NDEF_ID))
    private var NDEF_URI_BYTES = NDEF_URI.toByteArray()
    private var NDEF_URI_LEN = fillByteArrayToFixedDimension(
        BigInteger.valueOf(NDEF_URI_BYTES.size.toLong()).toByteArray(),
        2,
    )

    private var mNdefRecordFile: ByteArray = byteArrayOf()

    private var mAppSelected // true when SELECT_APPLICATION detected
            = false

    private var mCcSelected // true when SELECT_CAPABILITY_CONTAINER detected
            = false

    private var mNdefSelected // true when SELECT_NDEF_FILE detected
            = false


    override fun onCreate() {
        super.onCreate()
        mAppSelected = false
        mCcSelected = false
        mNdefSelected = false
        // default NDEF-message
        // default NDEF-message
        val DEFAULT_MESSAGE =
            "This is the default message from NfcHceNdelEmulator. If you want to change the message use the tab 'Send' to enter an individual message."
        val ndefDefaultMessage: NdefMessage = getNdefMessage(DEFAULT_MESSAGE)!!
        // the maximum length is 246 so do not extend this value
        // the maximum length is 246 so do not extend this value
        val nlen = ndefDefaultMessage.byteArrayLength
        mNdefRecordFile = ByteArray(nlen + 2)
        mNdefRecordFile[0] = ((nlen and 0xff00) / 256).toByte()
        mNdefRecordFile[1] = (nlen and 0xff).toByte()
        System.arraycopy(
            ndefDefaultMessage.toByteArray(),
            0,
            mNdefRecordFile,
            2,
            ndefDefaultMessage.byteArrayLength
        )
    }

    private fun getNdefMessage(ndefData: String): NdefMessage? {
        if (ndefData.isEmpty()) {
            return null
        }
        val ndefRecord: NdefRecord = NdefRecord.createTextRecord("en", ndefData)
        return NdefMessage(ndefRecord)
    }


    private fun getNdefUrlMessage(ndefData: String): NdefMessage? {
        if (ndefData.isEmpty()) {
            return null
        }
        val ndefRecord: NdefRecord = NdefRecord.createUri(ndefData)
        return NdefMessage(ndefRecord)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.hasExtra("ndefMessage")!!) {
            KEY_MSG = intent.getStringExtra("ndefMessage")!!
            NDEF_URI =
                NdefMessage(createTextRecord("en", intent.getStringExtra("ndefMessage")!!, NDEF_ID))

            NDEF_URI_BYTES = NDEF_URI.toByteArray()
            NDEF_URI_LEN = fillByteArrayToFixedDimension(
                BigInteger.valueOf(NDEF_URI_BYTES.size.toLong()).toByteArray(),
                2,
            )
            Log.i(TAG, "$NDEF_URI_BYTES")
            Log.i(TAG, "$NDEF_URI_LEN")
        }

        Log.i(TAG, "onStartCommand() | NDEF$NDEF_URI")

        return Service.START_STICKY
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Log.d("HCE", "processCommandApdu");
        Log.d("HCE", Arrays.toString(commandApdu));
        val message = if (KEY_MSG.isEmpty()) "Hello" else KEY_MSG
        return message.toByteArray();
    //        Log.d(TAG, "commandApdu: " + "".bytesToHex(commandApdu))
//        //if (Arrays.equals(SELECT_APP, commandApdu)) {
//        // check if commandApdu qualifies for SELECT_APPLICATION
//        //if (Arrays.equals(SELECT_APP, commandApdu)) {
//        // check if commandApdu qualifies for SELECT_APPLICATION
//        if (Arrays.equals(APDU_SELECT, commandApdu)) {
//            mAppSelected = true
//            mCcSelected = false
//            mNdefSelected = false
//            Log.d(TAG, "responseApdu: " + "".bytesToHex(A_OKAY))
//            return A_OKAY
//            // check if commandApdu qualifies for SELECT_CAPABILITY_CONTAINER
//        } else if (mAppSelected && Arrays.equals(CAPABILITY_CONTAINER_OK, commandApdu)) {
//            mCcSelected = true
//            mNdefSelected = false
//            Log.d(TAG, "responseApdu: " + "".bytesToHex(A_OKAY))
//            return A_OKAY
//            // check if commandApdu qualifies for SELECT_NDEF_FILE
//        } else if (mAppSelected && Arrays.equals(SELECT_NDEF_FILE, commandApdu)) {
//            // NDEF
//            mCcSelected = false
//            mNdefSelected = true
//            Log.d(TAG, "responseApdu: " + "".bytesToHex(A_OKAY))
//            return A_OKAY
//            // check if commandApdu qualifies for // READ_BINARY
//        } else if (commandApdu[0] === 0x00.toByte() && commandApdu[1] === 0xb0.toByte()) {
//            // READ_BINARY
//            // get the offset an le (length) data
//            //System.out.println("** " + Utils.bytesToHex(commandApdu) + " in else if (commandApdu[0] == (byte)0x00 && commandApdu[1] == (byte)0xb0) {");
//            val offset = (0x00ff and commandApdu[2].toInt()) * 256 + (0x00ff and commandApdu[3]
//                .toInt())
//            val le = 0x00ff and commandApdu[4].toInt()
//            val responseApdu = ByteArray(le + A_OKAY.size)
//            if (mCcSelected && offset == 0 && le == CAPABILITY_CONTAINER_FILE.size) {
//                System.arraycopy(CAPABILITY_CONTAINER_FILE, offset, responseApdu, 0, le)
//                System.arraycopy(A_OKAY, 0, responseApdu, le, A_OKAY.size)
//                Log.d(TAG, "responseApdu: " + "".bytesToHex(responseApdu))
//                return responseApdu
//            } else if (mNdefSelected) {
//                if (offset + le <= mNdefRecordFile.size) {
//                    System.arraycopy(mNdefRecordFile, offset, responseApdu, 0, le)
//                    System.arraycopy(A_OKAY, 0, responseApdu, le, A_OKAY.size)
//                    Log.d(TAG, "responseApdu: " + "".bytesToHex(responseApdu))
//                    return responseApdu
//                }
//            }
//        }
//
//        // The tag should return different errors for different reasons
//        // this emulation just returns the general error message
//
//        // The tag should return different errors for different reasons
//        // this emulation just returns the general error message
//        Log.d(TAG, "responseApdu: " + "".bytesToHex(A_ERROR))
//        return A_ERROR
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "onDeactivated() Fired! Reason: $reason")
        mAppSelected = false;
        mCcSelected = false;
        mNdefSelected = false;
    }

    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    private fun ByteArray.toHex(): String {
        val result = StringBuffer()

        forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }

        return result.toString()
    }

    fun String.bytesToHex(bytes: ByteArray): String {
        val result = StringBuffer()
        for (b in bytes) result.append(
            ((b.toInt() and 0xff) + 0x100).toString(16).substring(1)
        )
        return result.toString()
    }

    fun String.hexStringToByteArray(): ByteArray {
        val result = ByteArray(length / 2)

        for (i in indices step 2) {
            val firstIndex = HEX_CHARS.indexOf(this[i])
            val secondIndex = HEX_CHARS.indexOf(this[i + 1])

            val octet = firstIndex.shl(4).or(secondIndex)
            result[i.shr(1)] = octet.toByte()
        }

        return result
    }

    private fun createTextRecord(language: String, text: String, id: ByteArray): NdefRecord {
        val languageBytes: ByteArray
        val textBytes: ByteArray
        try {
            languageBytes = language.toByteArray(charset("US-ASCII"))
            textBytes = text.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            throw AssertionError(e)
        }

        val recordPayload = ByteArray(1 + (languageBytes.size and 0x03F) + textBytes.size)

        recordPayload[0] = (languageBytes.size and 0x03F).toByte()
        System.arraycopy(languageBytes, 0, recordPayload, 1, languageBytes.size and 0x03F)
        System.arraycopy(
            textBytes,
            0,
            recordPayload,
            1 + (languageBytes.size and 0x03F),
            textBytes.size,
        )

        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, id, recordPayload)
    }

    private fun fillByteArrayToFixedDimension(array: ByteArray, fixedSize: Int): ByteArray {
        if (array.size == fixedSize) {
            return array
        }

        val start = byteArrayOf(0x00.toByte())
        val filledArray = ByteArray(start.size + array.size)
        System.arraycopy(start, 0, filledArray, 0, start.size)
        System.arraycopy(array, 0, filledArray, start.size, array.size)
        return fillByteArrayToFixedDimension(filledArray, fixedSize)
    }
}