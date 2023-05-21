plugins {
    id("memo.android.feature")
}

setPackage("com.memo.post.on.map")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)
    implementation(YandexMapsLib.full)
    implementation(LandscapistLib.glide)

    implementation(project(Core.data))
    implementation(project(Core.commonModels))
    implementation(project(Core.design))
}
