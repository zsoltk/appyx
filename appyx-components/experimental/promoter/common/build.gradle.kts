import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.bumble.appyx.multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlin-parcelize")
    id("appyx-publish-multiplatform")
    id("com.google.devtools.ksp")
}

appyx {
    androidNamespace.set("com.bumble.appyx.components.experimental.promoter.common")
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
        }
    }
    js(IR) {
        // Adding moduleName as a workaround for this issue: https://youtrack.jetbrains.com/issue/KT-51942
        moduleName = "appyx-components-experimental-promoter-commons"
        browser()
        binaries.executable()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        // Adding moduleName as a workaround for this issue: https://youtrack.jetbrains.com/issue/KT-51942
        moduleName = "appyx-components-experimental-promoter-commons-wa"
        browser {
            // Refer to this Slack thread for more details: https://kotlinlang.slack.com/archives/CDFP59223/p1702977410505449?thread_ts=1702668737.674499&cid=CDFP59223
            testTask {
                useKarma {
                    useChromeHeadless()
                    useConfigDirectory(project.projectDir.resolve("karma.config.d").resolve("wasm"))
                }
            }
        }
        binaries.executable()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":appyx-interactions:appyx-interactions"))
                implementation(project(":appyx-components:experimental:cards:cards"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val desktopMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
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
    add("kspJs", project(":ksp:appyx-processor"))
    add("kspWasmJs", project(":ksp:appyx-processor"))
    add("kspIosArm64", project(":ksp:appyx-processor"))
    add("kspIosX64", project(":ksp:appyx-processor"))
    add("kspIosSimulatorArm64", project(":ksp:appyx-processor"))
}
