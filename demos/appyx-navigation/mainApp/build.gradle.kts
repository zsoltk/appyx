import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.bumble.appyx.multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTarget.get()
            }
        }
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
        }
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        // Adding moduleName as a workaround for this issue: https://youtrack.jetbrains.com/issue/KT-51942
        moduleName = "demo-appyx-navigation-main-wa"
        browser()
        binaries.executable()
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ios"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(compose.foundation)
            api(compose.material3)
            api(compose.runtime)
            api(project(":appyx-interactions:appyx-interactions"))
            api(project(":demos:image-loader:loader"))
            api(project(":utils:utils-customisations"))
            api(project(":utils:utils-material3"))
            api(project(":utils:utils-multiplatform"))
            implementation(libs.kotlinx.serialization.json)
            implementation(project(":appyx-components:experimental:cards:cards"))
            implementation(project(":appyx-components:experimental:promoter:promoter"))
            implementation(project(":appyx-components:standard:backstack:backstack"))
            implementation(project(":appyx-components:standard:spotlight:spotlight"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            val composeBom = project.dependencies.platform(libs.compose.bom)

            api(libs.androidx.core)
            implementation(composeBom)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.lifecycle.java8)
            implementation(libs.coil.compose)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui.tooling)
            implementation(libs.google.material)
            implementation(project(":appyx-components:standard:backstack:backstack"))
        }
        val desktopMain by getting {
            dependencies {
                api(compose.foundation)
                api(compose.material)
                api(compose.preview)
                api(compose.runtime)
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.coroutines.swing)
                implementation(project(":appyx-components:standard:backstack:backstack"))
                implementation(project(":appyx-interactions:appyx-interactions"))
                implementation(project(":appyx-navigation:appyx-navigation"))
                implementation(project(":demos:common"))
            }
        }
        val wasmJsMain by getting {
            dependencies {
                implementation(project(":demos:common"))
                implementation(project(":demos:mkdocs:common"))
                implementation(project(":demos:mkdocs:appyx-components:common"))
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}

android {
    namespace = "com.bumble.appyx.demos.navigation.main"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    signingConfigs {
        create("sampleConfig") { // debug is already created
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.findByName("sampleConfig")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // if we ever publish, we should create a more secure signingConfig
            signingConfig = signingConfigs.findByName("sampleConfig")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "com.bumble.appyx.demos.navigation.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AppyxNavigationDesktop"
            packageVersion = properties["library.version"].toString().split("-")[0]
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
        }
    }
}

compose.experimental {
    web.application {}
}

dependencies {
    add("kspCommonMainMetadata", project(":ksp:appyx-processor"))
    add("kspAndroid", project(":ksp:appyx-processor"))
    add("kspDesktop", project(":ksp:appyx-processor"))
    add("kspWasmJs", project(":ksp:appyx-processor"))
    add("kspIosArm64", project(":ksp:appyx-processor"))
    add("kspIosX64", project(":ksp:appyx-processor"))
    add("kspIosSimulatorArm64", project(":ksp:appyx-processor"))
}
