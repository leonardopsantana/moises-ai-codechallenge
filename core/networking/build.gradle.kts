plugins {
    id("com.moisesai.android-library")
    alias(libs.plugins.ksp)
}
android {
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        debug {
            buildConfigField("String", "HOST", Config.BuildField.base_url)

        }
        getByName("release") {
            buildConfigField("String", "HOST", Config.BuildField.base_url)
        }
    }
}
dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.koin)
    implementation(libs.koin.annotations)
    ksp(libs.koin.compiler)
    testImplementation(libs.bundles.test)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}