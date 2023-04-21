plugins {
    id("org.jetbrains.compose")
    id("com.android.library")
    kotlin("android")
}


android {
    namespace = "com.bumble.appyx.components.demos.android"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    packagingOptions {
        resources.excludes.apply {
            add("META-INF/LICENSE.md")
            add("META-INF/LICENSE-notice.md")
        }
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)

    api(project(":appyx-interactions:android"))
    api(project(":appyx-interactions:common"))
    api(project(":demos:common"))

    implementation(project(":appyx-components:demos:common"))
    implementation(composeBom)
    androidTestImplementation(libs.compose.ui.test.junit4)

    androidTestImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testRuntimeOnly(libs.junit.vintage)

    androidTestImplementation(libs.compose.ui.test.manifest)
}