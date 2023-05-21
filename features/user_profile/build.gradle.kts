plugins {
    id("memo.android.feature")
    id("com.google.devtools.ksp")
}

setPackage("com.memo.features.user_profile")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)
    implementation(ComposeLib.iconsExtended)
    implementation(OtherLib.collapsingToolbar)
    implementation(LandscapistLib.glide)

    ksp(MoshiLib.kotlinCodegen)

    implementation(project(Core.data))
    implementation(project(Core.design))
    implementation(project(Core.commonModels))
    implementation(project(Core.network))
    implementation(project(Core.utils))
    implementation(project(Core.storage))
}
