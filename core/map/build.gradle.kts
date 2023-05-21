plugins {
    id("memo.android.core")
}

setPackage("com.memo.core.map")

useCompose()

dependencies {
    implementation(AndroidXLib.lifecycleRuntimeCompose)
    implementation(YandexMapsLib.full)
}
