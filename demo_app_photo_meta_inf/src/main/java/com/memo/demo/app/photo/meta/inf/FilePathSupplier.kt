package com.memo.demo.app.photo.meta.inf

import android.content.Context
import androidx.core.util.Supplier
import java.io.File

class FilePathSupplier(context: Context) : Supplier<String> {

    private val context = context.applicationContext

    override fun get(): String {
        return File(context.cacheDir.absolutePath, FILENAME).absolutePath
    }

    private companion object {
        private const val FILENAME = "report.txt"
    }
}
