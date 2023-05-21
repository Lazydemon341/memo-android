package com.memo.demo.app.photo.meta.inf

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread

interface PhotosMetaDataReporter {

    @WorkerThread
    fun getSelectedImagesMetaInfo(context: Context, uris: List<Uri>): List<String>

    @WorkerThread
    fun getAllImagesInfo(context: Context): List<String>
}
