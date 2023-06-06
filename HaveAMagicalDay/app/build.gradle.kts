import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("org.jlleitschuh.gradle.ktlint") version "11.4.0"
    application
}

application {
    mainClass.set("com.michaeltchuang.cron.App")
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.5.2")
    implementation("com.algorand:algosdk:2.1.0")
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
