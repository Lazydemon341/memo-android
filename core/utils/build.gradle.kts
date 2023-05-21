plugins {
    id("memo.android.core")
}

setPackage("com.memo.core.utils")

dependencies {
    implementation(AndroidXLib.coreKtx)
    implementation(KotlinLib.coroutines)
    implementation(RetrofitLib.core)
}
