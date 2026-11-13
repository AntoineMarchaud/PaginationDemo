plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.org.jetbrains.kotlin.serializable)
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        optIn.add("kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation(libs.javax.inject)

    // Paging kotlin
    implementation(libs.paging.common)

    // Test
    testImplementation(libs.bundles.test)
}
