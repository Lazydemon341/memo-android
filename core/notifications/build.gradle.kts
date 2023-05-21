plugins {
    id("memo.android.core")
}

setPackage("com.memo.notifications")
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.core)
}
