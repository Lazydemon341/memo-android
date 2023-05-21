plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.chat")
useCompose()
useDiWithKapt()

dependencies {
    implementation(DILib.hiltNavigationCompose)
    implementation(ComposeLib.iconsExtended)
    implementation(LandscapistLib.glide)
    implementation(PagingLib.runtime)
    implementation(PagingLib.compose)

    implementation(project(Core.design))
    implementation(project(Core.data))
    implementation(project(Core.commonModels))
    implementation(project(Core.network))
    implementation(project(Core.utils))
}
