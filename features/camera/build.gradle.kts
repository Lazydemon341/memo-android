plugins {
    id("memo.android.feature")
}

setPackage("com.memo.features.camera")
useCompose()
useDiWithKapt()

dependencies {
    implementation(AccompanistLib.permissions)
    implementation(AndroidXLib.activity)
    implementation(AndroidXLib.constraintLayoutCompose)
    implementation(CameraViewLib.core)
    implementation(ComposeLib.iconsExtended)
    implementation(DILib.hiltNavigationCompose)
    implementation(KotlinLib.coroutines)

    implementation(project(Core.design))
    implementation(project(Core.data))
}
