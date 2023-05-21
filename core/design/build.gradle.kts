plugins {
    id("memo.android.core")
}

setPackage("com.memo.core.design")

useCompose()

dependencies {
    api(ComposeLib.iconsExtended)
}
