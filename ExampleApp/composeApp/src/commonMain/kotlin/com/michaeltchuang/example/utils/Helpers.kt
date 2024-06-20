package com.michaeltchuang.example.utils

import android.content.Context
import android.util.Log

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

fun truncatedValidatorName(str: String): String {
    val maxLength = 35
    if (str.endsWith(".algo").not() || str.length > maxLength) {
        return "${str.substring(0,5)} ... ${str.substring(str.length - 6)}"
    } else {
        return str
    }
}
