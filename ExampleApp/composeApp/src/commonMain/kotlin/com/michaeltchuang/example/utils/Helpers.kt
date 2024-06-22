package com.michaeltchuang.example.utils

import android.content.Context
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun getJsonDataFromAsset(
    context: Context,
    fileName: String,
): String? {
    val jsonString: String
    try {
        jsonString =
            context.assets
                .open(fileName)
                .bufferedReader()
                .use { it.readText() }
    } catch (e: Exception) {
        Log.e("PlayCoinFlipperScreen", e.toString())
        return null
    }
    return jsonString
}

fun truncatedAlgorandAddress(str: String): String {
    val maxLength = 35
    if (str.endsWith(".algo")) {
        return str
    } else if (str.length > maxLength) {
        return "${str.substring(0,5)} ... ${str.substring(str.length - 6)}"
    } else {
        return "empty"
    }
}

fun getValidatorListBoxName(id: Long): ByteArray {
    val prefix = byteArrayOf('v'.toByte())
    val ibytes = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(id).array()
    return prefix + ibytes
}
