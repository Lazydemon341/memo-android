plugins {
    id("memo.android.core")
}

setPackage("com.memo.core.data")
useDiWithKapt()

dependencies {
    implementation(KotlinLib.coroutines)

    implementation(project(Core.network))
    implementation(project(Core.commonModels))
    implementation(project(Core.utils))
    implementation(project(Core.dataStore))
    implementation(project(Core.storage))
}
