@file:Suppress("UnstableApiUsage")

plugins {
    id("com.moisesai.application")
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.player)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.core.networking)

    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    implementation(libs.bundles.koin)
    implementation(libs.bundles.androidSupport)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.kotlin)

    implementation(libs.lottie)
    implementation(libs.androidx.splashscreen)
    testImplementation(libs.bundles.test)
}