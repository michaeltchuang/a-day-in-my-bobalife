import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    application
}

application {
    mainClass.set("com.michaeltchuang.cron.App")
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:5.0.0")
    implementation("com.algorand:algosdk:2.5.0")
    testImplementation("junit:junit:4.13.2")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
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
