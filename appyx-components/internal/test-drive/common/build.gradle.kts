import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.bumble.appyx.multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

appyx {
    androidNamespace.set("com.bumble.appyx.components.internal.testdrive")
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
        moduleName = "appyx-components-internal-testdrive-common"
        browser()
        binaries.executable()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        // Adding moduleName as a workaround for this issue: https://youtrack.jetbrains.com/issue/KT-51942
        moduleName = "appyx-components-internal-testdrive-common-wa"
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
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(project(":appyx-interactions:appyx-interactions"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val desktopMain by getting
        val jsMain by getting
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
}
