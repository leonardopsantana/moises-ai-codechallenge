@file:Suppress("UnstableApiUsage")
import extensions.getBundle
import extensions.getLibrary
import extensions.setupCompose

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("com.moisesai.android-library")
}

android {
    setupCompose(libs)
}

dependencies {
    implementation(platform(libs.getLibrary("compose.bom")))
    androidTestImplementation(platform(libs.getLibrary("compose.bom")))

    implementation(libs.getBundle("compose"))
    debugImplementation(libs.getLibrary("compose.ui.tooling"))
}