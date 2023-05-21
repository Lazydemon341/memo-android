plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.memories.upload")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)
    implementation(LandscapistLib.glide)
    implementation(YandexMapsLib.full)

    implementation(project(Core.commonModels))
    implementation(project(Core.design))
    implementation(project(Core.glide))
    implementation(project(Core.utils))
    implementation(project(Core.data))
    implementation(project(Core.map))
    implementation(project(Core.network))
    implementation(project(Core.storage))
}
