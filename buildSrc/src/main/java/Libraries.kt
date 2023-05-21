import utils.lib

object AndroidXLib {
    val core by lib("androidx.core:core", Versions.androidXCore)
    val coreKtx by lib("androidx.core:core-ktx", Versions.androidXCore)
    val activity by lib("androidx.activity:activity-ktx", Versions.androidXActivity)
    val appcompat by lib("androidx.appcompat:appcompat", Versions.androidXAppCompat)
    val lifecycleViewModel by lib(
        "androidx.lifecycle:lifecycle-viewmodel-ktx",
        Versions.androidXLifecycle
    )
    val lifecycleViewModelCompose by lib(
        "androidx.lifecycle:lifecycle-viewmodel-compose",
        Versions.androidXLifecycle
    )
    val lifecycleRuntimeCompose by lib(
        "androidx.lifecycle:lifecycle-runtime-compose",
        Versions.androidXLifecycle
    )
    val dataStoreProto by lib("androidx.datastore:datastore", Versions.androidXDataStore)
    val dataStorePreferences by lib("androidx.datastore:datastore", Versions.androidXDataStore)
    val activityCompose by lib("androidx.activity:activity-compose", Versions.activityCompose)
    val constraintLayoutCompose by lib(
        "androidx.constraintlayout:constraintlayout-compose",
        Versions.constraintLayoutCompose
    )
    val exif by lib("androidx.exifinterface:exifinterface", Versions.androidXExif)
}

object NavigationLib {
    val navigationUi by lib("androidx.navigation:navigation-ui-ktx", Versions.navigation)
    val navigationFragment by lib(
        "androidx.navigation:navigation-fragment-ktx",
        Versions.navigation
    )
    val navigationCompose by lib("androidx.navigation:navigation-compose", Versions.navigation)
    val navigationRuntime by lib("androidx.navigation:navigation-runtime-ktx", Versions.navigation)
    val navigationCommon by lib("androidx.navigation:navigation-common-ktx", Versions.navigation)
}

object KotlinLib {
    val std by lib("org.jetbrains.kotlin:kotlin-stdlib-jdk8", Versions.kotlin)
    val coroutines by lib(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core",
        Versions.kotlinCoroutines
    )
    val coroutinesAndroid by lib(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android",
        Versions.kotlinCoroutines
    )
}

object TestLib {
    val junit by lib("junit:junit", Versions.junit)
}

object LogLib {
    val timber by lib("com.jakewharton.timber:timber", Versions.timber)
}

object ComposeLib {
    val bom by lib("androidx.compose:compose-bom", Versions.composeBom)
    val foundation by lib("androidx.compose.foundation:foundation")
    val material3 by lib("androidx.compose.material3:material3")
    val runtime by lib("androidx.compose.runtime:runtime")
    val ui by lib("androidx.compose.ui:ui")
    val uiTooling by lib("androidx.compose.ui:ui-tooling")
    val uiToolingDebug by lib("androidx.compose.ui:ui-tooling-preview")
    val uiUtil by lib("androidx.compose.ui:ui-util")
    val iconsExtended by lib("androidx.compose.material:material-icons-extended")
}

object DILib {
    val hilt by lib("com.google.dagger:hilt-android", Versions.hilt)
    val kapt by lib("com.google.dagger:hilt-android-compiler", Versions.hilt)
    val hiltNavigationCompose by lib(
        "androidx.hilt:hilt-navigation-compose",
        Versions.hiltNavigation
    )
}

object RetrofitLib {
    val core by lib("com.squareup.retrofit2:retrofit", Versions.retrofit)
    val moshiConverter by lib("com.squareup.retrofit2:converter-moshi", Versions.retrofit)
}

object MoshiLib {
    val core by lib("com.squareup.moshi:moshi", Versions.moshi)
    val kotlinCodegen by lib("com.squareup.moshi:moshi-kotlin-codegen", Versions.moshi)
}

object OkHttpLib {
    val loggingInterceptor by lib("com.squareup.okhttp3:logging-interceptor", Versions.okHttp)
}

object GlideLib {
    val core by lib("com.github.bumptech.glide:glide", Versions.glide)
    val ksp by lib("com.github.bumptech.glide:ksp", Versions.glide)
    val okhttpIntegration by lib("com.github.bumptech.glide:okhttp3-integration", Versions.glide)
}

object AccompanistLib {
    val permissions by lib("com.google.accompanist:accompanist-permissions", Versions.accompanist)
}

object CameraViewLib {
    val core by lib("com.otaliastudios:cameraview", Versions.cameraView)
}

object LandscapistLib {
    val glide by lib("com.github.skydoves:landscapist-glide", Versions.landscapistGlide)
}

object OtherLib {
    val collapsingToolbar by lib("me.onebone:toolbar-compose", Versions.collapsingToolbar)
    val zxing by lib("com.journeyapps:zxing-android-embedded", Versions.zxing)
}

object PagingLib {
    val runtime by lib("androidx.paging:paging-runtime", Versions.paging)
    val compose by lib("androidx.paging:paging-compose", Versions.pagingCompose)
}

object JavaXLib {
    val inject by lib("javax.inject:javax.inject", Versions.javaXInject)
}

object YandexMapsLib {
    val full by lib("com.yandex.android:maps.mobile", "${Versions.yandexMaps}-full")
}

object TensorflowLite {
    val taskVision by lib("org.tensorflow:tensorflow-lite-task-vision", Versions.tensorFlowLite)
    val gpuDelegate by lib("org.tensorflow:tensorflow-lite-gpu-delegate-plugin", Versions.tensorFlowLite)
    val gpu by lib("org.tensorflow:tensorflow-lite-gpu", Versions.tensorflowLiteGpu)
}
