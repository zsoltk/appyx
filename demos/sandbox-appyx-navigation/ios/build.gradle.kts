plugins {
    id("com.bumble.appyx.multiplatform")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
}

kotlin {
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
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(project(":demos:sandbox-appyx-navigation:common"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}

dependencies {
    add("kspIosArm64", project(":ksp:appyx-processor"))
    add("kspIosX64", project(":ksp:appyx-processor"))
    add("kspIosSimulatorArm64", project(":ksp:appyx-processor"))
}
