import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.dokka).apply(false)
//    alias(libs.plugins.vanniktech.maven.publish).apply(false)
    application
}
dependencies {
    implementation(libs.mongodb.driver)
    implementation(libs.mongodb.bson)
    implementation(libs.kotlinx.coroutines.core)
}
kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
application {
    mainClass.set("net.kigawa.fonsole.Main")
}