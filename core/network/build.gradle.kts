plugins {
    id("memo.android.core")
    id("com.google.devtools.ksp")
}

setPackage("com.memo.core.network")
useDiWithKapt()

dependencies {
    api(RetrofitLib.core)
    api(MoshiLib.core)

    implementation(RetrofitLib.moshiConverter)
    implementation(OkHttpLib.loggingInterceptor)

    ksp(MoshiLib.kotlinCodegen)

    implementation(project(Core.dataStore))
    implementation(project(Core.utils))
}
