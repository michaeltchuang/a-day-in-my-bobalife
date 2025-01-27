import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
    application
}

application {
    mainClass.set("com.michaeltchuang.cron.App")
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.algosdk)
    testImplementation(libs.junit)
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
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
