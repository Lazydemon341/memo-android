import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    compileOnly(gradleApi())

    val androidPluginVersion = "7.4.2"
    val kotlinPluginVersion = "1.8.10"
    val ktlintPluginVersion = "11.0.0"
    val javaPoetVersion = "1.13.0"

    implementation("com.android.tools.build:gradle:$androidPluginVersion")
    implementation(kotlin("gradle-plugin", kotlinPluginVersion))
    implementation("org.jlleitschuh.gradle:ktlint-gradle:$ktlintPluginVersion")
    // hilt doesn't work without this (https://github.com/google/dagger/issues/3068)
    implementation("com.squareup:javapoet:$javaPoetVersion")
}

gradlePlugin {
    plugins {
        register("memo.android.app") {
            id = "memo.android.app"
            implementationClass = "plugins.AppModulePlugin"
        }
        register("memo.android.feature") {
            id = "memo.android.feature"
            implementationClass = "plugins.FeatureModulePlugin"
        }
        register("memo.android.core") {
            id = "memo.android.core"
            implementationClass = "plugins.CoreModulePlugin"
        }
        register("memo.kotlin") {
            id = "memo.kotlin"
            implementationClass = "plugins.PureKotlinModulePlugin"
        }
    }
}
