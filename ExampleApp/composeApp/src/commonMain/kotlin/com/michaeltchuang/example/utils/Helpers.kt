package com.michaeltchuang.example.utils

import android.content.Context
import android.util.Log

fun getJsonDataFromAsset(
    context: Context,
    fileName: String,
): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        Log.e("PlayCoinFlipperScreen", e.toString())
        return null
    }
    return jsonString
}
