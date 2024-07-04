plugins {
    id("com.android.library")
    // JetBrains
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.serializable)
    // Sqldelight
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "com.amarchaud.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

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

    buildFeatures {
        buildConfig = true
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

    // KTX
    implementation(libs.core.ktx)

    // Ktor
    implementation(libs.bundles.ktor)

    // sqldelight
    implementation(libs.bundles.sqldelight)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)

    // Test
    testImplementation(libs.bundles.test)
}

sqldelight {
    databases {
        register("PaginationDemoDatabase") {
            packageName.set("com.amarchaud.database")
        }
    }
}
