plugins {
    id("memo.android.feature")
}

setPackage("com.memo.photo.widget")
useDiWithKapt()

dependencies {
    implementation(KotlinLib.coroutines)
    implementation(AndroidXLib.core)
    implementation(AndroidXLib.coreKtx)
    implementation(GlideLib.core)

    implementation(project(Core.network))
    implementation(project(Core.utils))
    implementation(project(Core.location))
}
