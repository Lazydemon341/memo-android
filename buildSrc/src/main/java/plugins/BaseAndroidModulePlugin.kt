package plugins

import AppConfig
import KotlinLib
import LogLib
import androidExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

abstract class BaseAndroidModulePlugin(
    private val isLibrary: Boolean = true,
) : Plugin<Project> {

    override fun apply(project: Project) {
        project.applyKotlinPlugin()
        project.applyAndroidPlugin()
        project.applyKtLintPlugin()
        project.androidExtension.apply {
            setConfig()
            setCompileOptions(project)
            setBuildTypes()
        }
        project.addDefaultDependencies()
    }

    private fun Project.applyAndroidPlugin() {
        if (isLibrary) {
            plugins.apply("com.android.library")
        } else {
            plugins.apply("com.android.application")
        }
    }

    private fun Project.applyKotlinPlugin() {
        plugins.apply("kotlin-android")
    }

    private fun Project.applyKtLintPlugin() {
        plugins.apply("org.jlleitschuh.gradle.ktlint")
    }

    private fun BaseExtension.setConfig() {
        compileSdkVersion(AppConfig.compileSdk)
        buildToolsVersion = AppConfig.buildToolsVersion

        defaultConfig {
            minSdk = AppConfig.minSdk
            targetSdk = AppConfig.targetSdk
            versionCode = AppConfig.versionCode
            versionName = AppConfig.versionName
        }
    }

    private fun BaseExtension.setCompileOptions(project: Project) {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        project.tasks.withType(KotlinCompile::class.java).configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    private fun BaseExtension.setBuildTypes() {
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            getByName("debug") {
                isMinifyEnabled = false
            }
        }
    }

    private fun Project.addDefaultDependencies() {
        dependencies {
            add("implementation", KotlinLib.std)
            add("implementation", LogLib.timber)
        }
    }
}
