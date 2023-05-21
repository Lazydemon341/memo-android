plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.memo.demo.app.photo.meta.inf"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.memo.demo.app.photo.meta.inf"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

useCompose()

dependencies {
    implementation(KotlinLib.coroutines)
    implementation(AndroidXLib.core)
    implementation(AndroidXLib.coreKtx)
    implementation(AndroidXLib.appcompat)
    implementation(AndroidXLib.activityCompose)
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.exifinterface:exifinterface:1.3.5")
}
