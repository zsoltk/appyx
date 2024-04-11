import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("com.bumble.appyx.multiplatform")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
}

kotlin {
    js(IR) {
        moduleName = "appyx-demos-navigation-web"
        browser()
        binaries.executable()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "appyx-demos-navigation-web-wa"
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
                implementation(project(":demos:common"))
                implementation(project(":demos:appyx-navigation:common"))
                implementation(project(":appyx-interactions:appyx-interactions"))
                implementation(project(":appyx-navigation:appyx-navigation"))
                implementation(project(":appyx-components:standard:backstack:backstack"))
                implementation(project(":demos:mkdocs:appyx-components:common"))
                implementation(project(":demos:mkdocs:common"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}

compose.experimental {
    web.application {}
}

dependencies {
    add("kspCommonMainMetadata", project(":ksp:appyx-processor"))
    add("kspJs", project(":ksp:appyx-processor"))
    add("kspWasmJs", project(":ksp:appyx-processor"))
}

tasks.register<Copy>("jsCopyResources") {
    // Dirs containing files we want to copy
    from("../common/src/commonMain/resources")

    // Output for web resources
    into("${layout.buildDirectory.get().asFile}/processedResources/js/main")

    include("**/*")
}

tasks.named("jsBrowserProductionExecutableDistributeResources") {
    dependsOn("jsCopyResources")
}

tasks.named("jsMainClasses") {
    dependsOn("jsCopyResources")
}

tasks.register<Copy>("wasmJsCopyResources") {
    // Dirs containing files we want to copy
    from("../common/src/commonMain/resources")

    // Output for web resources
    into("${layout.buildDirectory.get().asFile}/processedResources/wasmJs/main")

    include("**/*")
}

tasks.named("wasmJsBrowserProductionExecutableDistributeResources") {
    dependsOn("wasmJsCopyResources")
}

tasks.named("wasmJsMainClasses") {
    dependsOn("wasmJsCopyResources")
}