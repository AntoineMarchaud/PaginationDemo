plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.org.jetbrains.kotlin.serializable)
}

kotlin {
    jvmToolchain(17)

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation(libs.javax.inject)

    // Paging kotlin
    implementation(libs.paging.common)

    // Test
    testImplementation(libs.bundles.test)
}
