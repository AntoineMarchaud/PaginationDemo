plugins {
    // Android
    alias(libs.plugins.com.android.application)
    // JetBrains
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.serializable)
    alias(libs.plugins.compose.compiler)
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    namespace = "com.amarchaud.PaginationDemo"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.amarchaud.PaginationDemo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlin.sourceSets.configureEach {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }

    buildFeatures {
        compose = true
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/io.netty.versions.properties"
            excludes += "/META-INF/INDEX.LIST"
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":data"))

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

    // Test
    testImplementation(libs.bundles.test)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
}
