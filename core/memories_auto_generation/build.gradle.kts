plugins {
    id("memo.android.core")
}

android {
    androidResources {
        noCompress("tflite")
    }
}

setPackage("com.memo.core.memories.auto.generation")
useDiWithKapt()

dependencies {
    implementation(AndroidXLib.coreKtx)
    implementation(KotlinLib.coroutines)
    implementation(AndroidXLib.exif)

    implementation(project(Core.commonModels))
    implementation(project(Core.data))
    implementation(project(Core.location))

    implementation(TensorflowLite.taskVision)
    implementation(TensorflowLite.gpuDelegate)
    implementation(TensorflowLite.gpu)
}
