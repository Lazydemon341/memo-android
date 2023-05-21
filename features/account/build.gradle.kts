plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.account")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(DILib.hiltNavigationCompose)

    implementation(project(Core.data))
    implementation(project(Core.commonModels))
}
