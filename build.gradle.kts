import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.dokka).apply(false)
//    alias(libs.plugins.vanniktech.maven.publish).apply(false)
    application
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(libs.mongodb.driver)
    implementation(libs.mongodb.bson)
    implementation(libs.dotenv)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.logback)
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