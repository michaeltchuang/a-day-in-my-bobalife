import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.8.10")
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.5.2")
    implementation("com.algorand:algosdk:2.0.0")
}

fun buildVersionCode(): Int {
    val df = SimpleDateFormat("yyyyMMddHH")
    df.timeZone = TimeZone.getTimeZone("PST")
    return df.format(Date()).toInt()
}

fun buildVersionString(): String {
    val df = SimpleDateFormat("yyyyMMdd")
    df.timeZone = TimeZone.getTimeZone("PST")
    return df.format(Date())
}
