@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.commonMain
import org.gradle.kotlin.dsl.commonTest
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.internal.platform.wasm.WasmPlatforms.wasmJs
import kotlin.text.set

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                jvmTarget.set(JvmTarget.valueOf(libs.versions.jvmVersion.get()))
            }
        }
    }

    jvm()

    wasmJs {
        browser()
        outputModuleName.set("Kurrency")
    }

    js(IR) {
        browser()
        nodejs()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Kurrency"
            isStatic = true
            export(libs.cedar.logging)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                api(libs.cedar.logging)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.12.0")
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation("androidx.test.ext:junit:1.2.1")
                implementation("androidx.test:runner:1.6.2")
                implementation("androidx.test:rules:1.6.1")
                implementation(libs.kotlin.test)
            }
        }

        val jvmMain by getting

        val wasmJsMain by getting
        val jsMain by getting

        val webMain by creating {
            dependsOn(commonMain)
            wasmJsMain.dependsOn(this)
            jsMain.dependsOn(this)
        }

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

android {
    namespace = "com.chilinoodles.kurrency"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isIncludeAndroidResources = false
    }
}
