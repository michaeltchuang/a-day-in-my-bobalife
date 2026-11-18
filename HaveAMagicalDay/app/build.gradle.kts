import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
    application
}

application {
    mainClass.set("com.michaeltchuang.cron.App")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.falcon.signatures.mobile)
    implementation(libs.jna)
    implementation(libs.json)

    // AlgoKit Core Rust libraries (UniFFI/Kotlin bindings), replacing the Java algosdk for
    // account derivation, transaction composition/signing, and msgpack encoding.
    implementation(files("libs/algokit_composer.jar", "libs/algokit_crypto.jar", "libs/algokit_transact.jar"))

    testImplementation(libs.junit)
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
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
