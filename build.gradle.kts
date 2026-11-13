// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    // JetBrains
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.org.jetbrains.kotlin.serializable) apply false
    // Hilt
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

subprojects {
    // Mockito's inline agent appends to the boot classpath: disable class data sharing
    // to avoid the "Sharing is only supported for boot loader classes" JVM warning
    tasks.withType<Test>().configureEach {
        jvmArgs("-Xshare:off")
    }
}
