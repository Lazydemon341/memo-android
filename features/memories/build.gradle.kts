plugins {
    id("memo.android.feature")
    id("com.google.devtools.ksp")
}

setPackage("com.memo.features.memories")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(ComposeLib.iconsExtended)
    implementation(DILib.hiltNavigationCompose)
    implementation(LandscapistLib.glide)
    implementation(YandexMapsLib.full)

    ksp(MoshiLib.kotlinCodegen)

    implementation(project(Core.design))
    implementation(project(Core.glide))
    implementation(project(Core.utils))
    implementation(project(Core.data))
    implementation(project(Core.map))
    implementation(project(Core.network))
    implementation(project(Core.location))
}
