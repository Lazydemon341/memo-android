plugins {
    id("memo.android.core")
    id("com.google.devtools.ksp")
}

setPackage("com.memo.core.glide")
useCompose()
useDiWithKapt()

dependencies {
    implementation(GlideLib.core)
    implementation(GlideLib.okhttpIntegration)

    ksp(GlideLib.ksp)
}
