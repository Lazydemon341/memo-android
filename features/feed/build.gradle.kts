plugins {
    id("memo.android.feature")
}

setPackage("com.memo.feed")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)
    implementation(LandscapistLib.glide)
    implementation(PagingLib.runtime)
    implementation(PagingLib.compose)

    implementation(project(Core.data))
    implementation(project(Core.commonModels))
    implementation(project(Core.design))
}
