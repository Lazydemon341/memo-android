plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.memories.generation")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)
    implementation(LandscapistLib.glide)
    implementation(AccompanistLib.permissions)

    implementation(project(Core.memoriesAutoGeneration))
    implementation(project(Core.commonModels))
    implementation(project(Core.design))
    implementation(project(Core.glide))
    implementation(project(Core.utils))
    implementation(project(Core.data))
    implementation(project(Core.notifications))
}
