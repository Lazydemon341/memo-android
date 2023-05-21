plugins {
    id("memo.android.app")
}

setPackage("com.memo.app")
useCompose()
useDiWithKapt()

android {
    defaultConfig {
        buildConfigField(
            "String", "YANDEX_MAPS_API_KEY",
            properties["YANDEX_MAPS_API_KEY"].toString()
        )
    }
}

dependencies {
    // libraries
    implementation(AndroidXLib.appcompat)
    implementation(AndroidXLib.core)
    implementation(AndroidXLib.coreKtx)
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(KotlinLib.coroutines)
    implementation(NavigationLib.navigationCompose)
    implementation(NavigationLib.navigationUi)
    implementation(YandexMapsLib.full)

    testImplementation(TestLib.junit)

    // modules
    implementation(project(Features.account))
    implementation(project(Features.memories))
    implementation(project(Features.camera))
    implementation(project(Features.publishPhoto))
    implementation(project(Features.photoWidget))
    implementation(project(Features.friendship))
    implementation(project(Features.userProfile))
    implementation(project(Features.feed))
    implementation(project(Features.chat))
    implementation(project(Features.chatsList))
    implementation(project(Features.memoriesGeneration))
    implementation(project(Features.friendsMap))
    implementation(project(Features.postOnMap))
    implementation(project(Features.memoriesUpload))

    implementation(project(Core.utils))
    implementation(project(Core.design))
    implementation(project(Core.dataStore))
    implementation(project(Core.data))
    implementation(project(Core.network))
    implementation(project(Core.glide))
    implementation(project(Core.commonModels))
    implementation(project(Core.notifications))
    implementation(project(Core.memoriesAutoGeneration))
    implementation(project(Core.location))
}
