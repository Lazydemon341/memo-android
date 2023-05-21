plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.friends.map")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)
    implementation(YandexMapsLib.full)
    implementation(GlideLib.core)

    implementation(project(Core.design))
    implementation(project(Core.data))
    implementation(project(Core.commonModels))
    implementation(project(Core.map))
}
