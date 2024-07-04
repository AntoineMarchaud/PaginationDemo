plugins {
    id("com.android.library")
    // JetBrains
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.serializable)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.amarchaud.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    kotlin.sourceSets.configureEach {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}


kotlin {
    jvmToolchain(17)
}


dependencies {
    implementation(project(":domain"))

    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)

    // Navigation
    implementation(libs.bundles.navigation)

    // KTX
    implementation(libs.activity.ktx)
    implementation(libs.core.ktx)
    implementation(libs.fragment.ktx)

    // Image
    implementation(libs.bundles.coil)

    // Maps
    implementation(libs.osmdroid)

    // Test
    testImplementation(libs.bundles.test)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)
}
