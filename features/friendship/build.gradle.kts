plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.friendship")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)
    implementation(OtherLib.zxing)

    implementation(project(Core.data))
    implementation(project(Core.design))
    implementation(project(Core.commonModels))
    implementation(project(Core.dataStore))
    implementation(project(Core.network))
    implementation(project(Core.utils))
}
