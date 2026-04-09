@file:Suppress("UnstableApiUsage")

plugins {
    id("com.moisesai.android-library")
    id("com.moisesai.compose")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(projects.core.navigation)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.networking)

    implementation(libs.bundles.networking)
    implementation(libs.bundles.koin)
    implementation(libs.koin.annotations)
    ksp(libs.koin.compiler)

    implementation(libs.roomRuntime)
    implementation(libs.bundles.androidSupport)
    implementation(libs.coil)
    implementation(libs.bundles.media3)

    testImplementation(libs.bundles.test)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}