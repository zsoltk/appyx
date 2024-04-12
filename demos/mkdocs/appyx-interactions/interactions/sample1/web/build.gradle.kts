import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.bumble.appyx.multiplatform")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "appyx-interactions-sample1-web-wa"
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
                implementation(project(":appyx-components:internal:test-drive:test-drive"))
                implementation(project(":utils:utils-multiplatform"))
            }
        }
        val wasmJsMain by getting {
            dependencies {
                implementation(project(":demos:mkdocs:common"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}

dependencies {
    add("kspCommonMainMetadata", project(":ksp:appyx-processor"))
    add("kspWasmJs", project(":ksp:appyx-processor"))
}
