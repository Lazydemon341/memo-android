plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.chats_list")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(ComposeLib.iconsExtended)
    implementation(DILib.hiltNavigationCompose)
    implementation(LandscapistLib.glide)

    implementation(project(Core.design))
    implementation(project(Core.data))
    implementation(project(Core.commonModels))
    implementation(project(Core.network))
    implementation(project(Core.utils))
}
