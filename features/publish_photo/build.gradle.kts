plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.publish_photo")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(AndroidXLib.constraintLayoutCompose)
    implementation(ComposeLib.iconsExtended)
    implementation(DILib.hiltNavigationCompose)
    implementation(LandscapistLib.glide)

    implementation(project(Core.commonModels))
    implementation(project(Core.data))
    implementation(project(Core.dataStore))
    implementation(project(Core.design))
    implementation(project(Core.network))
    implementation(project(Core.utils))
    implementation(project(Core.storage))
    implementation(project(Core.location))
}
