// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // #1 기초 세팅 이슈 참고
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.mannodermaus) apply false
    alias(libs.plugins.jlleitschuh) apply false
    alias(libs.plugins.fireBasePlugIn) apply false
    alias(libs.plugins.firebaseCrashlyticsPlugIn) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
