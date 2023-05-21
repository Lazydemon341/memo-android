@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("memo.android.core")
    id("com.squareup.wire") version Versions.wire
}

setPackage("com.memo.core.datastore")
useDiWithKapt()

wire {
    kotlin {}
    sourcePath {
        srcDir("src/main/proto")
    }
}

dependencies {
    implementation(AndroidXLib.dataStoreProto)

    implementation(project(Core.commonModels))
    implementation(project(Core.utils))
}
