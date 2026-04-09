@file:Suppress("UnstableApiUsage")
plugins {
    id("com.moisesai.android-library")
    id("com.moisesai.compose")
}

android {
    namespace = "com.moisesai.core.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.androidSupport)
    testImplementation(libs.bundles.test)
    implementation(libs.coil)
}