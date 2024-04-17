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
                implementation(project(":demos:appyx-navigation:common"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}

tasks.register<Copy>("copyResources") {
    // Dirs containing files we want to copy
    from("../common/src/commonMain/resources")

    // Output for iOS resources
    into("${layout.buildDirectory.get().asFile}/compose/ios/ios/compose-resources")

    include("**/*")
}

tasks.named("compileKotlinIosArm64") {
    dependsOn("copyResources")
}

tasks.named("compileKotlinIosSimulatorArm64") {
    dependsOn("copyResources")
}

tasks.named("compileKotlinIosX64") {
    dependsOn("copyResources")
}

dependencies {
    add("kspIosArm64", project(":ksp:appyx-processor"))
    add("kspIosX64", project(":ksp:appyx-processor"))
    add("kspIosSimulatorArm64", project(":ksp:appyx-processor"))
}
